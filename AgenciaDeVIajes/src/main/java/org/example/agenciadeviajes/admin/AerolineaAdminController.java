package org.example.agenciadeviajes.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.agenciadeviajes.dao.AerolineaDAO;
import org.example.agenciadeviajes.model.Aerolinea;

/**
 * Controlador CRUD para administración de Aerolíneas
 */
public class AerolineaAdminController {

    @FXML private TableView<Aerolinea> tablaAerolineas;
    @FXML private TableColumn<Aerolinea, Integer> colId;
    @FXML private TableColumn<Aerolinea, String> colNombre;
    @FXML private TableColumn<Aerolinea, String> colCodigo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigoIata;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private AerolineaDAO dao;
    private Aerolinea aerolineaSeleccionada;

    @FXML
    public void initialize() {
        try {
            this.dao = new AerolineaDAO();

            // Configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("idAerolinea"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoIata"));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaAerolineas.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarAerolinea(newVal)
            );

            System.out.println("[AerolineaAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[AerolineaAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[AerolineaAdminController.cargarTabla] Cargando aerolíneas...");
            java.util.List<Aerolinea> lista = dao.obtenerTodos();
            System.out.println("[AerolineaAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (Aerolinea a : lista) {
                if (a != null) {
                    System.out.println("  - " + a.getIdAerolinea() + ": " + a.getNombre() + " (" + a.getCodigoIata() + ")");
                }
            }

            ObservableList<Aerolinea> datos = FXCollections.observableArrayList(lista);
            tablaAerolineas.setItems(datos);
            System.out.println("[AerolineaAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[AerolineaAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarAerolinea(Aerolinea aerolinea) {
        if (aerolinea == null) return;

        this.aerolineaSeleccionada = aerolinea;
        txtNombre.setText(aerolinea.getNombre());
        txtCodigoIata.setText(aerolinea.getCodigoIata());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearAerolinea() {
        try {
            String nombre = txtNombre.getText().trim();
            String codigoIata = txtCodigoIata.getText().trim();

            if (nombre.isEmpty() || codigoIata.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            Aerolinea a = new Aerolinea(0, nombre, codigoIata);
            if (dao.insertar(a)) {
                alert("Éxito", "Aerolínea creada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear la aerolínea");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarAerolinea() {
        try {
            if (aerolineaSeleccionada == null) {
                alert("Error", "Selecciona una aerolínea");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String codigoIata = txtCodigoIata.getText().trim();

            if (nombre.isEmpty() || codigoIata.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            aerolineaSeleccionada.setNombre(nombre);
            aerolineaSeleccionada.setCodigoIata(codigoIata);

            if (dao.actualizar(aerolineaSeleccionada)) {
                alert("Éxito", "Aerolínea actualizada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar la aerolínea");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarAerolinea() {
        try {
            if (aerolineaSeleccionada == null) {
                alert("Error", "Selecciona una aerolínea");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar esta aerolínea?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (dao.eliminar(aerolineaSeleccionada.getIdAerolinea())) {
                    alert("Éxito", "Aerolínea eliminada correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar la aerolínea");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtCodigoIata.clear();
        tablaAerolineas.getSelectionModel().clearSelection();
        aerolineaSeleccionada = null;
        btnActualizar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    @FXML
    private void regresar() {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("/org/example/agenciadeviajes/view/home-admin.fxml"));
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(vista, 1400, 850));
            stage.setTitle("Panel de Administración");
        } catch (Exception e) {
            System.err.println("[AerolineaAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
