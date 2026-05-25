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
import org.example.agenciadeviajes.dao.*;
import org.example.agenciadeviajes.model.*;

import java.math.BigDecimal;

/**
 * Controlador CRUD para administración de Autos
 */
public class AutoAdminController {

    @FXML private TableView<Auto> tablaAutos;
    @FXML private TableColumn<Auto, Integer> colId;
    @FXML private TableColumn<Auto, String> colModelo;
    @FXML private TableColumn<Auto, String> colProveedor;
    @FXML private TableColumn<Auto, String> colCiudad;
    @FXML private TableColumn<Auto, BigDecimal> colPrecio;

    @FXML private ComboBox<ModeloAuto> cbModelo;
    @FXML private ComboBox<String> cbProveedor;
    @FXML private ComboBox<Ciudad> cbCiudad;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbDivisa;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private AutoDAO dao;
    private ModeloAutoDAO modeloDAO;
    private ProveedorAutoDAO proveedorDAO;
    private CiudadDAO ciudadDAO;
    private DivisaDAO divisaDAO;
    private Auto autoSeleccionado;

    @FXML
    public void initialize() {
        try {
            this.dao = new AutoDAO();
            this.modeloDAO = new ModeloAutoDAO();
            this.proveedorDAO = new ProveedorAutoDAO();
            this.ciudadDAO = new CiudadDAO();
            this.divisaDAO = new DivisaDAO();

            // Configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("idAuto"));
            colModelo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModeloAuto().toString())
            );
            colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
            colCiudad.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCiudadDisponibilidad().getNombre())
            );
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioDia"));

            // Cargar datos en ComboBoxes
            java.util.List<ModeloAuto> modelos = modeloDAO.obtenerTodos();
            cbModelo.setItems(FXCollections.observableArrayList(modelos));

            java.util.List<String> proveedores = proveedorDAO.obtenerTodos();
            cbProveedor.setItems(FXCollections.observableArrayList(proveedores));

            java.util.List<Ciudad> ciudades = ciudadDAO.obtenerTodos();
            cbCiudad.setItems(FXCollections.observableArrayList(ciudades));

            java.util.List<Divisa> divisas = divisaDAO.obtenerTodos();
            java.util.List<String> codigosDivisa = new java.util.ArrayList<>();
            for (Divisa d : divisas) {
                codigosDivisa.add(d.getCodigo());
            }
            cbDivisa.setItems(FXCollections.observableArrayList(codigosDivisa));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaAutos.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarAuto(newVal)
            );

            System.out.println("[AutoAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[AutoAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[AutoAdminController.cargarTabla] Cargando autos...");
            java.util.List<Auto> lista = dao.obtenerTodos();
            System.out.println("[AutoAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (Auto a : lista) {
                if (a != null && a.getModeloAuto() != null) {
                    System.out.println("  - " + a.getIdAuto() + ": " + a.getModeloAuto().getMarca() + " " + a.getModeloAuto().getModelo());
                }
            }

            ObservableList<Auto> datos = FXCollections.observableArrayList(lista);
            tablaAutos.setItems(datos);
            System.out.println("[AutoAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[AutoAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarAuto(Auto auto) {
        if (auto == null) return;

        this.autoSeleccionado = auto;
        cbModelo.setValue(auto.getModeloAuto());
        cbProveedor.setValue(auto.getProveedor());
        cbCiudad.setValue(auto.getCiudadDisponibilidad());
        txtPrecio.setText(auto.getPrecioDia().toString());
        cbDivisa.setValue(auto.getCodigoDivisa());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearAuto() {
        try {
            ModeloAuto modelo = cbModelo.getValue();
            String proveedor = cbProveedor.getValue();
            Ciudad ciudad = cbCiudad.getValue();
            String precio = txtPrecio.getText().trim();
            String divisa = cbDivisa.getValue();

            if (modelo == null || proveedor == null || ciudad == null || precio.isEmpty() || divisa == null) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            BigDecimal precioBD = new BigDecimal(precio);
            int idProveedor = proveedorDAO.obtenerIdPorNombre(proveedor);

            Auto a = new Auto(0, modelo, proveedor, idProveedor, ciudad, precioBD, divisa);
            if (dao.insertar(a)) {
                alert("Éxito", "Auto creado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear el auto");
            }
        } catch (NumberFormatException e) {
            alert("Error", "El precio debe ser un número válido");
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarAuto() {
        try {
            if (autoSeleccionado == null) {
                alert("Error", "Selecciona un auto");
                return;
            }

            String precio = txtPrecio.getText().trim();

            if (precio.isEmpty()) {
                alert("Error", "El precio es requerido");
                return;
            }

            BigDecimal precioBD = new BigDecimal(precio);
            autoSeleccionado.setPrecioDia(precioBD);

            if (dao.actualizar(autoSeleccionado)) {
                alert("Éxito", "Auto actualizado correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar el auto");
            }
        } catch (NumberFormatException e) {
            alert("Error", "El precio debe ser un número válido");
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarAuto() {
        try {
            if (autoSeleccionado == null) {
                alert("Error", "Selecciona un auto");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar este auto?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (dao.eliminar(autoSeleccionado.getIdAuto())) {
                    alert("Éxito", "Auto eliminado correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar el auto");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        cbModelo.setValue(null);
        cbProveedor.setValue(null);
        cbCiudad.setValue(null);
        txtPrecio.clear();
        cbDivisa.setValue(null);
        tablaAutos.getSelectionModel().clearSelection();
        autoSeleccionado = null;
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
            System.err.println("[AutoAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
