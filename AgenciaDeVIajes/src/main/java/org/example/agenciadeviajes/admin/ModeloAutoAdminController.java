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
import org.example.agenciadeviajes.dao.ModeloAutoDAO;
import org.example.agenciadeviajes.model.ModeloAuto;

/**
 * Controlador CRUD para administración de Modelos de Autos
 */
public class ModeloAutoAdminController {

    @FXML private TableView<ModeloAuto> tablaModelos;
    @FXML private TableColumn<ModeloAuto, Integer> colId;
    @FXML private TableColumn<ModeloAuto, String> colMarca;
    @FXML private TableColumn<ModeloAuto, String> colModelo;
    @FXML private TableColumn<ModeloAuto, String> colCategoria;

    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private ComboBox<String> cbCategoria;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private ModeloAutoDAO dao;
    private CategoriaAutoDAO categoriaDAO;
    private ModeloAuto modeloSeleccionado;

    @FXML
    public void initialize() {
        try {
            this.dao = new ModeloAutoDAO();
            this.categoriaDAO = new CategoriaAutoDAO();

            // Configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("idModelo"));
            colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
            colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
            colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

            // Cargar categorías en ComboBox
            java.util.List<String> categorias = categoriaDAO.obtenerTodos();
            cbCategoria.setItems(FXCollections.observableArrayList(categorias));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaModelos.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarModelo(newVal)
            );

            System.out.println("[ModeloAutoAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[ModeloAutoAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[ModeloAutoAdminController.cargarTabla] Cargando modelos...");
            java.util.List<ModeloAuto> lista = dao.obtenerTodos();
            System.out.println("[ModeloAutoAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (ModeloAuto m : lista) {
                if (m != null) {
                    System.out.println("  - " + m.getIdModelo() + ": " + m.getMarca() + " " + m.getModelo() + " (" + m.getCategoria() + ")");
                }
            }

            ObservableList<ModeloAuto> datos = FXCollections.observableArrayList(lista);
            tablaModelos.setItems(datos);
            System.out.println("[ModeloAutoAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[ModeloAutoAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarModelo(ModeloAuto modelo) {
        if (modelo == null) return;

        this.modeloSeleccionado = modelo;
        txtMarca.setText(modelo.getMarca());
        txtModelo.setText(modelo.getModelo());
        cbCategoria.setValue(modelo.getCategoria());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearModelo() {
        try {
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String categoria = cbCategoria.getValue();

            if (marca.isEmpty() || modelo.isEmpty() || categoria == null) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            ModeloAuto m = new ModeloAuto(0, marca, modelo, categoria);
            if (dao.insertar(m)) {
                alert("Éxito", "Modelo creado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear el modelo");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarModelo() {
        try {
            if (modeloSeleccionado == null) {
                alert("Error", "Selecciona un modelo");
                return;
            }

            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String categoria = cbCategoria.getValue();

            if (marca.isEmpty() || modelo.isEmpty() || categoria == null) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            modeloSeleccionado.setMarca(marca);
            modeloSeleccionado.setModelo(modelo);
            modeloSeleccionado.setCategoria(categoria);

            if (dao.actualizar(modeloSeleccionado)) {
                alert("Éxito", "Modelo actualizado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar el modelo");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarModelo() {
        try {
            if (modeloSeleccionado == null) {
                alert("Error", "Selecciona un modelo");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar este modelo?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (dao.eliminar(modeloSeleccionado.getIdModelo())) {
                    alert("Éxito", "Modelo eliminado correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar el modelo");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtMarca.clear();
        txtModelo.clear();
        cbCategoria.setValue(null);
        tablaModelos.getSelectionModel().clearSelection();
        modeloSeleccionado = null;
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
            System.err.println("[ModeloAutoAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
