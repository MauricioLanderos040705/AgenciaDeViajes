package org.example.agenciadeviajes.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.agenciadeviajes.dao.CiudadDAO;
import org.example.agenciadeviajes.dao.HotelDAO;
import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Hotel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador simple para CRUD de Hoteles
 */
public class HotelAdminController {

    @FXML private TableView<Hotel> tablaHoteles;
    @FXML private TableColumn<Hotel, Integer> colId;
    @FXML private TableColumn<Hotel, String> colNombre;
    @FXML private TableColumn<Hotel, String> colCiudad;
    @FXML private TableColumn<Hotel, Integer> colEstrellas;

    @FXML private TextField txtNombre;
    @FXML private ComboBox<Ciudad> cbCiudad;
    @FXML private Spinner<Integer> spEstrellas;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbDivisa;
    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private HotelDAO hotelDAO = new HotelDAO();
    private CiudadDAO ciudadDAO = new CiudadDAO();
    private Hotel seleccionado = null;

    @FXML
    public void initialize() {
        // Configurar tabla
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdHotel()).asObject());
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colCiudad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudad().getNombre()));
        colEstrellas.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getEstrellas()).asObject());

        // Cargar datos
        cargarCiudades();
        cargarDivisas();
        cargarTabla();

        // Eventos de tabla
        tablaHoteles.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) seleccionarHotel(nuevo);
        });

        // Spinner
        spEstrellas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
    }

    private void cargarCiudades() {
        cbCiudad.setItems(FXCollections.observableArrayList(ciudadDAO.obtenerTodos()));
    }

    private void cargarDivisas() {
        cbDivisa.setItems(FXCollections.observableArrayList("MXN", "USD", "EUR", "CAD"));
    }

    private void cargarTabla() {
        tablaHoteles.setItems(FXCollections.observableArrayList(hotelDAO.obtenerTodos()));
    }

    private void seleccionarHotel(Hotel h) {
        seleccionado = h;
        txtNombre.setText(h.getNombre());
        cbCiudad.setValue(h.getCiudad());
        spEstrellas.getValueFactory().setValue(h.getEstrellas());
        txtPrecio.setText(h.getPrecioNoche().toString());
        cbDivisa.setValue(h.getCodigoDivisa());
        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearHotel() {
        if (!validar()) return;

        Hotel h = new Hotel();
        h.setNombre(txtNombre.getText());
        h.setCiudad(cbCiudad.getValue());
        h.setEstrellas(spEstrellas.getValue());
        h.setPrecioNoche(new BigDecimal(txtPrecio.getText()));
        h.setHabitacionesDisponibles(0);
        h.setCodigoDivisa(cbDivisa.getValue());

        if (hotelDAO.insertar(h)) {
            alert("Éxito", "Hotel creado");
            limpiar();
            cargarTabla();
        } else {
            alert("Error", "No se pudo crear");
        }
    }

    @FXML
    private void actualizarHotel() {
        if (seleccionado == null || !validar()) return;

        seleccionado.setNombre(txtNombre.getText());
        seleccionado.setCiudad(cbCiudad.getValue());
        seleccionado.setEstrellas(spEstrellas.getValue());
        seleccionado.setPrecioNoche(new BigDecimal(txtPrecio.getText()));
        seleccionado.setCodigoDivisa(cbDivisa.getValue());

        if (hotelDAO.actualizar(seleccionado)) {
            alert("Éxito", "Hotel actualizado");
            limpiar();
            cargarTabla();
        } else {
            alert("Error", "No se pudo actualizar");
        }
    }

    @FXML
    private void eliminarHotel() {
        if (seleccionado == null) return;

        if (confirmacion("¿Eliminar hotel?")) {
            if (hotelDAO.eliminar(seleccionado.getIdHotel())) {
                alert("Éxito", "Hotel eliminado");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo eliminar");
            }
        }
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtPrecio.clear();
        cbCiudad.setValue(null);
        cbDivisa.setValue(null);
        spEstrellas.getValueFactory().setValue(3);
        seleccionado = null;
        tablaHoteles.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    private boolean validar() {
        return !txtNombre.getText().isEmpty() &&
               cbCiudad.getValue() != null &&
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
