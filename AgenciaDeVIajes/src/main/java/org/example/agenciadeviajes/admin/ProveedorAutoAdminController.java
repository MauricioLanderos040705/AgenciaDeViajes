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
import org.example.agenciadeviajes.dao.ProveedorAutoDAO;

/**
 * Controlador CRUD para administración de Proveedores de Autos
 */
public class ProveedorAutoAdminController {

    @FXML private TableView<String> tablaProveedores;
    @FXML private TableColumn<String, String> colProveedor;

    @FXML private TextField txtProveedor;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private ProveedorAutoDAO dao;
    private String proveedorSeleccionado;

    @FXML
    public void initialize() {
        try {
            this.dao = new ProveedorAutoDAO();

            // Configurar columnas
            colProveedor.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue())
            );

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaProveedores.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarProveedor(newVal)
            );

            System.out.println("[ProveedorAutoAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[ProveedorAutoAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[ProveedorAutoAdminController.cargarTabla] Cargando proveedores...");
            java.util.List<String> lista = dao.obtenerTodos();
            System.out.println("[ProveedorAutoAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (String prov : lista) {
                System.out.println("  - " + prov);
            }

            ObservableList<String> datos = FXCollections.observableArrayList(lista);
            tablaProveedores.setItems(datos);
            System.out.println("[ProveedorAutoAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[ProveedorAutoAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarProveedor(String proveedor) {
        if (proveedor == null) return;

        this.proveedorSeleccionado = proveedor;
        txtProveedor.setText(proveedor);

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearProveedor() {
        try {
            String nombre = txtProveedor.getText().trim();

            if (nombre.isEmpty()) {
                alert("Error", "El nombre es requerido");
                return;
            }

            if (dao.insertar(nombre)) {
                alert("Éxito", "Proveedor creado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear el proveedor");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarProveedor() {
        try {
            if (proveedorSeleccionado == null) {
                alert("Error", "Selecciona un proveedor");
                return;
            }

            String nombre = txtProveedor.getText().trim();

            if (nombre.isEmpty()) {
                alert("Error", "El nombre es requerido");
                return;
            }

            // Get ID from nombre
            int id = dao.obtenerIdPorNombre(proveedorSeleccionado);
            if (id > 0 && dao.actualizar(id, nombre)) {
                alert("Éxito", "Proveedor actualizado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar el proveedor");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarProveedor() {
        try {
            if (proveedorSeleccionado == null) {
                alert("Error", "Selecciona un proveedor");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar este proveedor?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                int id = dao.obtenerIdPorNombre(proveedorSeleccionado);
                if (id > 0 && dao.eliminar(id)) {
                    alert("Éxito", "Proveedor eliminado correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar el proveedor");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtProveedor.clear();
        tablaProveedores.getSelectionModel().clearSelection();
        proveedorSeleccionado = null;
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
            System.err.println("[ProveedorAutoAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
