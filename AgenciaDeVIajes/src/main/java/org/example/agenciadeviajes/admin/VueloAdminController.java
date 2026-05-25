package org.example.agenciadeviajes.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.agenciadeviajes.dao.AerolineaDAO;
import org.example.agenciadeviajes.dao.CiudadDAO;
import org.example.agenciadeviajes.dao.VueloDAO;
import org.example.agenciadeviajes.model.Aerolinea;
import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador simple para CRUD de Vuelos
 */
public class VueloAdminController {

    @FXML private TableView<Vuelo> tablaVuelos;
    @FXML private TableColumn<Vuelo, Integer> colId;
    @FXML private TableColumn<Vuelo, String> colAerolinea;
    @FXML private TableColumn<Vuelo, String> colOrigen;
    @FXML private TableColumn<Vuelo, String> colDestino;

    @FXML private ComboBox<Aerolinea> cbAerolinea;
    @FXML private ComboBox<Ciudad> cbOrigen;
    @FXML private ComboBox<Ciudad> cbDestino;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtAsientos;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbDivisa;
    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private VueloDAO vueloDAO = new VueloDAO();
    private AerolineaDAO aerolineaDAO = new AerolineaDAO();
    private CiudadDAO ciudadDAO = new CiudadDAO();
    private Vuelo seleccionado = null;

    @FXML
    public void initialize() {
        // Configurar tabla
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdVuelo()).asObject());
        colAerolinea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAerolinea().getNombre()));
        colOrigen.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudadOrigen().getNombre()));
        colDestino.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudadDestino().getNombre()));

        // Cargar datos
        cbAerolinea.setItems(FXCollections.observableArrayList(aerolineaDAO.obtenerTodos()));
        List<Ciudad> ciudades = ciudadDAO.obtenerTodos();
        cbOrigen.setItems(FXCollections.observableArrayList(ciudades));
        cbDestino.setItems(FXCollections.observableArrayList(ciudades));
        cbDivisa.setItems(FXCollections.observableArrayList("MXN", "USD", "EUR", "CAD"));
        cargarTabla();

        // Eventos de tabla
        tablaVuelos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) seleccionarVuelo(nuevo);
        });
    }

    private void cargarTabla() {
        tablaVuelos.setItems(FXCollections.observableArrayList(vueloDAO.obtenerTodos()));
    }

    private void seleccionarVuelo(Vuelo v) {
        seleccionado = v;
        cbAerolinea.setValue(v.getAerolinea());
        cbOrigen.setValue(v.getCiudadOrigen());
        cbDestino.setValue(v.getCiudadDestino());
        dpFecha.setValue(v.getFechaSalida().toLocalDate());
        txtAsientos.setText(String.valueOf(v.getAsientosDisponibles()));
        txtPrecio.setText(v.getPrecioAsiento().toString());
        cbDivisa.setValue(v.getCodigoDivisa());
        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearVuelo() {
        if (!validar()) return;

        Vuelo v = new Vuelo();
        v.setAerolinea(cbAerolinea.getValue());
        v.setCiudadOrigen(cbOrigen.getValue());
        v.setCiudadDestino(cbDestino.getValue());
        v.setFechaSalida(dpFecha.getValue().atStartOfDay());
        v.setFechaLlegada(dpFecha.getValue().atStartOfDay().plusHours(2));
        v.setAsientosDisponibles(Integer.parseInt(txtAsientos.getText()));
        v.setPrecioAsiento(new BigDecimal(txtPrecio.getText()));
        v.setCodigoDivisa(cbDivisa.getValue());
        v.setEsRedondo(false);

        if (vueloDAO.insertar(v)) {
            alert("Éxito", "Vuelo creado");
            limpiar();
            cargarTabla();
        } else {
            alert("Error", "No se pudo crear");
        }
    }

    @FXML
    private void actualizarVuelo() {
        if (seleccionado == null || !validar()) return;

        seleccionado.setAerolinea(cbAerolinea.getValue());
        seleccionado.setCiudadOrigen(cbOrigen.getValue());
        seleccionado.setCiudadDestino(cbDestino.getValue());
        seleccionado.setFechaSalida(dpFecha.getValue().atStartOfDay());
        seleccionado.setFechaLlegada(dpFecha.getValue().atStartOfDay().plusHours(2));
        seleccionado.setAsientosDisponibles(Integer.parseInt(txtAsientos.getText()));
        seleccionado.setPrecioAsiento(new BigDecimal(txtPrecio.getText()));
        seleccionado.setCodigoDivisa(cbDivisa.getValue());

        if (vueloDAO.actualizar(seleccionado)) {
            alert("Éxito", "Vuelo actualizado");
            limpiar();
            cargarTabla();
        } else {
            alert("Error", "No se pudo actualizar");
        }
    }

    @FXML
    private void eliminarVuelo() {
        if (seleccionado == null) return;

        if (confirmacion("¿Eliminar vuelo?")) {
            if (vueloDAO.eliminar(seleccionado.getIdVuelo())) {
                alert("Éxito", "Vuelo eliminado");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo eliminar");
            }
        }
    }

    @FXML
    private void limpiar() {
        cbAerolinea.setValue(null);
        cbOrigen.setValue(null);
        cbDestino.setValue(null);
        dpFecha.setValue(null);
        txtAsientos.clear();
        txtPrecio.clear();
        cbDivisa.setValue(null);
        seleccionado = null;
        tablaVuelos.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private boolean validar() {
        return cbAerolinea.getValue() != null &&
               cbOrigen.getValue() != null &&
               cbDestino.getValue() != null &&
               dpFecha.getValue() != null &&
               !txtAsientos.getText().isEmpty() &&
               !txtPrecio.getText().isEmpty() &&
               cbDivisa.getValue() != null;
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private boolean confirmacion(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO)
                .showAndWait().get() == ButtonType.YES;
    }
}
