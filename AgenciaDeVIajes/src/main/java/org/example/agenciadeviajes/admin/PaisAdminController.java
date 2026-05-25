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
import org.example.agenciadeviajes.dao.PaisDAO;
import org.example.agenciadeviajes.model.Pais;

/**
 * Controlador CRUD para administración de Países
 */
public class PaisAdminController {

    @FXML private TableView<Pais> tablaPaises;
    @FXML private TableColumn<Pais, Integer> colId;
    @FXML private TableColumn<Pais, String> colNombre;
    @FXML private TableColumn<Pais, String> colCodigo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigoIso;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private PaisDAO dao;
    private Pais paisSeleccionado;

    @FXML
    public void initialize() {
        try {
            this.dao = new PaisDAO();

            // Configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("idPais"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoIso"));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaPaises.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarPais(newVal)
            );

            System.out.println("[PaisAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[PaisAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[PaisAdminController.cargarTabla] Cargando países...");
            java.util.List<Pais> lista = dao.obtenerTodos();
            System.out.println("[PaisAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (Pais p : lista) {
                if (p != null) {
                    System.out.println("  - " + p.getIdPais() + ": " + p.getNombre() + " (" + p.getCodigoIso() + ")");
                }
            }

            ObservableList<Pais> datos = FXCollections.observableArrayList(lista);
            tablaPaises.setItems(datos);
            System.out.println("[PaisAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[PaisAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarPais(Pais pais) {
        if (pais == null) return;

        this.paisSeleccionado = pais;
        txtNombre.setText(pais.getNombre());
        txtCodigoIso.setText(pais.getCodigoIso());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearPais() {
        try {
            String nombre = txtNombre.getText().trim();
            String codigoIso = txtCodigoIso.getText().trim();

            if (nombre.isEmpty() || codigoIso.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            Pais p = new Pais(0, nombre, codigoIso);
            if (dao.insertar(p)) {
                alert("Éxito", "País creado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear el país");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarPais() {
        try {
            if (paisSeleccionado == null) {
                alert("Error", "Selecciona un país");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String codigoIso = txtCodigoIso.getText().trim();

            if (nombre.isEmpty() || codigoIso.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            paisSeleccionado.setNombre(nombre);
            paisSeleccionado.setCodigoIso(codigoIso);

            if (dao.actualizar(paisSeleccionado)) {
                alert("Éxito", "País actualizado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar el país");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarPais() {
        try {
            if (paisSeleccionado == null) {
                alert("Error", "Selecciona un país");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar este país?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (dao.eliminar(paisSeleccionado.getIdPais())) {
                    alert("Éxito", "País eliminado correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar el país");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtCodigoIso.clear();
        tablaPaises.getSelectionModel().clearSelection();
        paisSeleccionado = null;
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
            System.err.println("[PaisAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
