package org.example.agenciadeviajes.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    @FXML private TableColumn<Vuelo, String> colSalida;
    @FXML private TableColumn<Vuelo, String> colLlegada;
    @FXML private TableColumn<Vuelo, String> colPrecio;
    @FXML private TableColumn<Vuelo, String> colDivisa;
    @FXML private TableColumn<Vuelo, Integer> colAsientos;
    @FXML private TableColumn<Vuelo, String> colRedondo;

    @FXML private ComboBox<Aerolinea> cbAerolinea;
    @FXML private ComboBox<Ciudad> cbOrigen;
    @FXML private ComboBox<Ciudad> cbDestino;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtAsientos;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbDivisa;
    @FXML private CheckBox chkRedondo;
    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnRegresar;
    @FXML private Button btnLimpiar;

    private VueloDAO vueloDAO = new VueloDAO();
    private AerolineaDAO aerolineaDAO = new AerolineaDAO();
    private CiudadDAO ciudadDAO = new CiudadDAO();
    private Vuelo seleccionado = null;

    @FXML
    public void initialize() {
        try {
            System.out.println("🔄 [VueloAdminController] Inicializando...");

            // Configurar tabla
            colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdVuelo()).asObject());
            colAerolinea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAerolinea().getNombre()));
            colOrigen.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudadOrigen().getNombre()));
            colDestino.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudadDestino().getNombre()));
            colSalida.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFechaSalida().toString()));
            colLlegada.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFechaLlegada().toString()));
            colPrecio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty("$" + c.getValue().getPrecioAsiento()));
            colDivisa.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCodigoDivisa()));
            colAsientos.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getAsientosDisponibles()).asObject());
            colRedondo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isEsRedondo() ? "Sí" : "No"));

            System.out.println("✓ Cell value factories configurados");

            // Cargar datos
            System.out.println("📋 Cargando aerolineas, ciudades y divisas...");
            cbAerolinea.setItems(FXCollections.observableArrayList(aerolineaDAO.obtenerTodos()));
            List<Ciudad> ciudades = ciudadDAO.obtenerTodos();
            cbOrigen.setItems(FXCollections.observableArrayList(ciudades));
            cbDestino.setItems(FXCollections.observableArrayList(ciudades));
            cbDivisa.setItems(FXCollections.observableArrayList("MXN", "USD", "EUR", "CAD"));

            System.out.println("✓ ComboBoxes cargados");
            cargarTabla();

            // Eventos de tabla
            tablaVuelos.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
                if (nuevo != null) seleccionarVuelo(nuevo);
            });

            System.out.println("✓ [VueloAdminController] Inicialización completada");
        } catch (Exception e) {
            System.err.println("❌ [VueloAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
            alert("Error de Inicialización", "Error al cargar datos: " + e.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("📋 [cargarTabla] Obteniendo vuelos de BD...");
            List<Vuelo> vuelos = vueloDAO.obtenerTodos();

            if (vuelos == null) {
                System.err.println("❌ vueloDAO.obtenerTodos() retornó NULL");
                alert("Error", "No se pudieron cargar los vuelos (NULL)");
                return;
            }

            System.out.println("✓ Vuelos obtenidos: " + vuelos.size());

            if (vuelos.isEmpty()) {
                System.out.println("⚠️ No hay vuelos en la BD");
            } else {
                for (Vuelo v : vuelos) {
                    System.out.println("  - " + v.getAerolinea().getNombre() + " (" +
                        v.getCiudadOrigen().getNombre() + " → " + v.getCiudadDestino().getNombre() + ")");
                }
            }

            tablaVuelos.setItems(FXCollections.observableArrayList(vuelos));
            System.out.println("✓ Tabla actualizada con " + vuelos.size() + " registros");
        } catch (Exception e) {
            System.err.println("❌ Error en cargarTabla: " + e.getMessage());
            e.printStackTrace();
            alert("Error", "Error al cargar tabla: " + e.getMessage());
        }
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
        chkRedondo.setSelected(v.isEsRedondo());
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
        v.setEsRedondo(chkRedondo.isSelected());

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
        seleccionado.setEsRedondo(chkRedondo.isSelected());

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
        chkRedondo.setSelected(false);
        seleccionado = null;
        tablaVuelos.getSelectionModel().clearSelection();
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home-admin.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 500));
            stage.setTitle("Panel de Administración");
        } catch (Exception e) {
            alert("Error", "No se pudo regresar: " + e.getMessage());
            e.printStackTrace();
        }
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
