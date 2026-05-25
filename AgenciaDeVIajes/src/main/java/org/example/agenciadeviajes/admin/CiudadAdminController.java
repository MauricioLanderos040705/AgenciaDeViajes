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
import org.example.agenciadeviajes.dao.CiudadDAO;
import org.example.agenciadeviajes.dao.PaisDAO;
import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Pais;

/**
 * Controlador CRUD para administración de Ciudades
 */
public class CiudadAdminController {

    @FXML private TableView<Ciudad> tablaCiudades;
    @FXML private TableColumn<Ciudad, Integer> colId;
    @FXML private TableColumn<Ciudad, String> colNombre;
    @FXML private TableColumn<Ciudad, String> colCodigo;
    @FXML private TableColumn<Ciudad, String> colPais;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCodigoIata;
    @FXML private ComboBox<Pais> cbPais;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;

    private CiudadDAO dao;
    private PaisDAO paisDAO;
    private Ciudad ciudadSeleccionada;

    @FXML
    public void initialize() {
        try {
            this.dao = new CiudadDAO();
            this.paisDAO = new PaisDAO();

            // Configurar columnas
            colId.setCellValueFactory(new PropertyValueFactory<>("idCiudad"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoIata"));
            colPais.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPais().getNombre())
            );

            // Cargar países en ComboBox
            java.util.List<Pais> paises = paisDAO.obtenerTodos();
            cbPais.setItems(FXCollections.observableArrayList(paises));

            // Cargar datos
            cargarTabla();

            // Listener para seleccionar fila
            tablaCiudades.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> seleccionarCiudad(newVal)
            );

            System.out.println("[CiudadAdminController] Inicializado correctamente");
        } catch (Exception e) {
            System.err.println("[CiudadAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTabla() {
        try {
            System.out.println("[CiudadAdminController.cargarTabla] Cargando ciudades...");
            java.util.List<Ciudad> lista = dao.obtenerTodos();
            System.out.println("[CiudadAdminController.cargarTabla] Registros encontrados: " + lista.size());

            for (Ciudad c : lista) {
                if (c != null && c.getPais() != null) {
                    System.out.println("  - " + c.getIdCiudad() + ": " + c.getNombre() + " (" + c.getPais().getNombre() + ")");
                }
            }

            ObservableList<Ciudad> datos = FXCollections.observableArrayList(lista);
            tablaCiudades.setItems(datos);
            System.out.println("[CiudadAdminController.cargarTabla] Tabla actualizada con " + datos.size() + " filas");
        } catch (Exception e) {
            System.err.println("[CiudadAdminController.cargarTabla] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seleccionarCiudad(Ciudad ciudad) {
        if (ciudad == null) return;

        this.ciudadSeleccionada = ciudad;
        txtNombre.setText(ciudad.getNombre());
        txtCodigoIata.setText(ciudad.getCodigoIata());
        cbPais.setValue(ciudad.getPais());

        btnActualizar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    @FXML
    private void crearCiudad() {
        try {
            String nombre = txtNombre.getText().trim();
            String codigoIata = txtCodigoIata.getText().trim();
            Pais pais = cbPais.getValue();

            if (nombre.isEmpty() || codigoIata.isEmpty() || pais == null) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            Ciudad c = new Ciudad(0, nombre, codigoIata, pais);
            if (dao.insertar(c)) {
                alert("Éxito", "Ciudad creada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo crear la ciudad");
            }
        } catch (Exception e) {
            alert("Error", "Error al crear: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarCiudad() {
        try {
            if (ciudadSeleccionada == null) {
                alert("Error", "Selecciona una ciudad");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String codigoIata = txtCodigoIata.getText().trim();
            Pais pais = cbPais.getValue();

            if (nombre.isEmpty() || codigoIata.isEmpty() || pais == null) {
                alert("Error", "Todos los campos son requeridos");
                return;
            }

            ciudadSeleccionada.setNombre(nombre);
            ciudadSeleccionada.setCodigoIata(codigoIata);
            ciudadSeleccionada.setPais(pais);

            if (dao.actualizar(ciudadSeleccionada)) {
                alert("Éxito", "Ciudad actualizada correctamente");
                limpiar();
                cargarTabla();
            } else {
                alert("Error", "No se pudo actualizar la ciudad");
            }
        } catch (Exception e) {
            alert("Error", "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarCiudad() {
        try {
            if (ciudadSeleccionada == null) {
                alert("Error", "Selecciona una ciudad");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Eliminar esta ciudad?", ButtonType.YES, ButtonType.NO);
            if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (dao.eliminar(ciudadSeleccionada.getIdCiudad())) {
                    alert("Éxito", "Ciudad eliminada correctamente");
                    limpiar();
                    cargarTabla();
                } else {
                    alert("Error", "No se pudo eliminar la ciudad");
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
        cbPais.setValue(null);
        tablaCiudades.getSelectionModel().clearSelection();
        ciudadSeleccionada = null;
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
            System.err.println("[CiudadAdminController.regresar] " + e.getMessage());
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
