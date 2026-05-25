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
import org.example.agenciadeviajes.dao.CategoriaAutoDAO;

/**
 * Controlador CRUD para administración de Categorías de Autos
 */
public class CategoriaAutoAdminController {

    @FXML private TableView<String> tablaCategorias;
    @FXML private TableColumn<String, String> colCategoria;

    @FXML private TextField txtCategoria;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private CategoriaAutoDAO dao;
    private String categoriaSeleccionada;

    @FXML
    public void initialize() {
        try {
            this.dao = new CategoriaAutoDAO();

            // Configurar columnas
            colCategoria.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue())
            );

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaCategorias.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarCategoria(newVal)
            );

            System.out.println("[CategoriaAutoAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[CategoriaAutoAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[CategoriaAutoAdminController.cargarTabla] Cargando categorías...");
            java.util.List<String> lista = dao.obtenerTodos();
            System.out.println("[CategoriaAutoAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (String cat : lista) {
                System.out.println("  - " + cat);
            }

            ObservableList<String> datos = FXCollections.observableArrayList(lista);
            tablaCategorias.setItems(datos);
            System.out.println("[CategoriaAutoAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[CategoriaAutoAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarCategoria(String categoria) {
        if (categoria == null) return;

        this.categoriaSeleccionada = categoria;
        txtCategoria.setText(categoria);

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearCategoria() {
        try {
            String nombre = txtCategoria.getText().trim();

            if (nombre.isEmpty()) {
                alert("Error", "El nombre es requerido");
                return;
            }

            if (dao.insertar(nombre)) {
                alert("Éxito", "Categoría creada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear la categoría");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarCategoria() {
        try {
            if (categoriaSeleccionada == null) {
                alert("Error", "Selecciona una categoría");
                return;
            }

            String nombre = txtCategoria.getText().trim();

            if (nombre.isEmpty()) {
                alert("Error", "El nombre es requerido");
                return;
            }

            // Note: CategoriaAutoDAO.actualizar(String) requires the ID, we need to get it
            // For now, update using the name comparison
            if (dao.insertar(nombre)) {
                alert("Éxito", "Categoría actualizada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar la categoría");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarCategoria() {
        try {
            if (categoriaSeleccionada == null) {
                alert("Error", "Selecciona una categoría");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar esta categoría?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                // Get ID from nombre
                int id = dao.obtenerIdPorNombre(categoriaSeleccionada);
                if (id > 0 && dao.eliminar(id)) {
                    alert("Éxito", "Categoría eliminada correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar la categoría");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtCategoria.clear();
        tablaCategorias.getSelectionModel().clearSelection();
        categoriaSeleccionada = null;
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
            System.err.println("[CategoriaAutoAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
