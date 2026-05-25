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
import org.example.agenciadeviajes.dao.DivisaDAO;
import org.example.agenciadeviajes.model.Divisa;

import java.math.BigDecimal;

/**
 * Controlador CRUD para administración de Divisas
 */
public class DivisaAdminController {

    @FXML private TableView<Divisa> tablaDivisas;
    @FXML private TableColumn<Divisa, String> colCodigo;
    @FXML private TableColumn<Divisa, String> colNombre;
    @FXML private TableColumn<Divisa, String> colSimbolo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtSimbolo;
    @FXML private TextField txtCodigo;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private DivisaDAO dao;
    private Divisa divisaSeleccionada;

    @FXML
    public void initialize() {
        try {
            this.dao = new DivisaDAO();

            // Configurar columnas
            colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colSimbolo.setCellValueFactory(new PropertyValueFactory<>("simbolo"));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaDivisas.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarDivisa(newVal)
            );

            System.out.println("[DivisaAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[DivisaAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[DivisaAdminController.cargarTabla] Cargando divisas...");
            java.util.List<Divisa> lista = dao.obtenerTodos();
            System.out.println("[DivisaAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (Divisa d : lista) {
                if (d != null) {
                    System.out.println("  - " + d.getCodigo() + ": " + d.getNombre() + " (" + d.getSimbolo() + ")");
                }
            }

            ObservableList<Divisa> datos = FXCollections.observableArrayList(lista);
            tablaDivisas.setItems(datos);
            System.out.println("[DivisaAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[DivisaAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarDivisa(Divisa divisa) {
        if (divisa == null) return;

        this.divisaSeleccionada = divisa;

        // DEBUG: Ver exactamente qué código se está guardando
        System.out.println("[DivisaAdminController.seleccionarDivisa] DEBUGGEAR SELECCIÓN:");
        System.out.println("  - Código RAW: '" + divisa.getCodigo() + "'");
        System.out.println("  - Código LENGTH: " + (divisa.getCodigo() != null ? divisa.getCodigo().length() : "null"));
        System.out.println("  - Código BYTES: " + (divisa.getCodigo() != null ? java.util.Arrays.toString(divisa.getCodigo().getBytes()) : "null"));
        System.out.println("  - Nombre: '" + divisa.getNombre() + "'");
        System.out.println("  - Símbolo: '" + divisa.getSimbolo() + "'");

        txtNombre.setText(divisa.getNombre());
        txtSimbolo.setText(divisa.getSimbolo());
        txtCodigo.setText(divisa.getCodigo());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearDivisa() {
        try {
            String nombre = txtNombre.getText().trim();
            String simbolo = txtSimbolo.getText().trim();
            String codigo = txtCodigo.getText().trim();

            if (nombre.isEmpty() || simbolo.isEmpty() || codigo.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            Divisa d = new Divisa(0, nombre, simbolo, codigo, BigDecimal.ZERO);
            if (dao.insertar(d)) {
                alert("Éxito", "Divisa creada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear la divisa");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarDivisa() {
        try {
            if (divisaSeleccionada == null) {
                alert("Error", "Selecciona una divisa");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String simbolo = txtSimbolo.getText().trim();
            String codigo = txtCodigo.getText().trim();

            if (nombre.isEmpty() || simbolo.isEmpty() || codigo.isEmpty()) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            System.out.println("[DivisaAdminController.actualizarDivisa] Intentando actualizar:");
            System.out.println("  - Código: " + codigo);
            System.out.println("  - Nombre anterior: " + divisaSeleccionada.getNombre());
            System.out.println("  - Nombre nuevo: " + nombre);
            System.out.println("  - Símbolo anterior: " + divisaSeleccionada.getSimbolo());
            System.out.println("  - Símbolo nuevo: " + simbolo);

            divisaSeleccionada.setNombre(nombre);
            divisaSeleccionada.setSimbolo(simbolo);
            divisaSeleccionada.setCodigo(codigo);

            boolean resultado = dao.actualizar(divisaSeleccionada);
            System.out.println("[DivisaAdminController.actualizarDivisa] Resultado DAO: " + resultado);

            if (resultado) {
                alert("Éxito", "Divisa actualizada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar la divisa - DAO retornó false");
            }
        } catch (Exception e) {
            System.err.println("[DivisaAdminController.actualizarDivisa] Exception: " + e.getMessage());
            e.printStackTrace();
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarDivisa() {
        try {
            if (divisaSeleccionada == null) {
                alert("Error", "Selecciona una divisa");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar esta divisa?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                // Divisa usa codigo_iso (String) como clave primaria
                System.out.println("[DivisaAdminController.eliminarDivisa] Intentando eliminar:");
                System.out.println("  - Código: " + divisaSeleccionada.getCodigo());

                boolean resultado = dao.eliminarPorCodigo(divisaSeleccionada.getCodigo());
                System.out.println("[DivisaAdminController.eliminarDivisa] Resultado DAO: " + resultado);

                if (resultado) {
                    alert("Éxito", "Divisa eliminada correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar la divisa - DAO retornó false");
                }
            }
        } catch (Exception e) {
            System.err.println("[DivisaAdminController.eliminarDivisa] Exception: " + e.getMessage());
            e.printStackTrace();
            alert("Error", "Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtSimbolo.clear();
        txtCodigo.clear();
        tablaDivisas.getSelectionModel().clearSelection();
        divisaSeleccionada = null;
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
            System.err.println("[DivisaAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
