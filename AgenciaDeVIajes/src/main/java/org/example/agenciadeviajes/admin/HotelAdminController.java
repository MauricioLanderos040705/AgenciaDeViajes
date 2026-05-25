package org.example.agenciadeviajes.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    @FXML private TableColumn<Hotel, BigDecimal> colPrecio;

    @FXML private TextField txtNombre;
    @FXML private ComboBox<Ciudad> cbCiudad;
    @FXML private Spinner<Integer> spEstrellas;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbDivisa;
    @FXML private Spinner<Integer> spHabitaciones;
    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnRegresar;
    @FXML private Button btnLimpiar;

    private HotelDAO hotelDAO = new HotelDAO();
    private CiudadDAO ciudadDAO = new CiudadDAO();
    private Hotel seleccionado = null;

    @FXML
    public void initialize() {
        try {
            System.out.println("🔄 [HotelAdminController] Inicializando...");

            // Configurar tabla
            colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdHotel()).asObject());
            colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
            colCiudad.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCiudad().getNombre()));
            colEstrellas.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getEstrellas()).asObject());
            colPrecio.setCellValueFactory(c -> {
                BigDecimal precio = c.getValue().getPrecioNoche();
                return new javafx.beans.property.SimpleObjectProperty<>(precio);
            });

            System.out.println("✓ Cell value factories configurados");

            // Cargar datos
            cargarCiudades();
            cargarDivisas();
            cargarTabla();

            System.out.println("✓ Datos cargados");

            // Eventos de tabla
            tablaHoteles.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
                if (nuevo != null) seleccionarHotel(nuevo);
            });

            // Spinners
            spEstrellas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
            spHabitaciones.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

            System.out.println("✓ [HotelAdminController] Inicialización completada");
        } catch (Exception e) {
            System.err.println("❌ [HotelAdminController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
            alert("Error de Inicialización", "Error al cargar datos: " + e.getMessage());
        }
    }

    private void cargarCiudades() {
        cbCiudad.setItems(FXCollections.observableArrayList(ciudadDAO.obtenerTodos()));
    }

    private void cargarDivisas() {
        cbDivisa.setItems(FXCollections.observableArrayList("MXN", "USD", "EUR", "CAD"));
    }

    private void cargarTabla() {
        try {
            System.out.println("📋 [cargarTabla] Obteniendo hoteles de BD...");
            List<Hotel> hoteles = hotelDAO.obtenerTodos();

            if (hoteles == null) {
                System.err.println("hotelDAO.obtenerTodos() retornó NULL");
                alert("Error", "No se pudieron cargar los hoteles (NULL)");
                return;
            }

            System.out.println("Hoteles obtenidos: " + hoteles.size());

            if (hoteles.isEmpty()) {
                System.out.println("No hay hoteles en la BD");
            } else {
                for (Hotel h : hoteles) {
                    System.out.println("  - " + h.getNombre() + " (" + h.getCiudad().getNombre() + ")");
                }
            }

            tablaHoteles.setItems(FXCollections.observableArrayList(hoteles));
            System.out.println("Tabla actualizada con " + hoteles.size() + " registros");
        } catch (Exception e) {
            System.err.println("Error en cargarTabla: " + e.getMessage());
            e.printStackTrace();
            alert("Error", "Error al cargar tabla: " + e.getMessage());
        }
    }

    private void seleccionarHotel(Hotel h) {
        seleccionado = h;
        txtNombre.setText(h.getNombre());
        cbCiudad.setValue(h.getCiudad());
        spEstrellas.getValueFactory().setValue(h.getEstrellas());
        txtPrecio.setText(h.getPrecioNoche().toString());
        cbDivisa.setValue(h.getCodigoDivisa());
        spHabitaciones.getValueFactory().setValue(h.getHabitacionesDisponibles());
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
        h.setHabitacionesDisponibles(spHabitaciones.getValue());
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
        seleccionado.setHabitacionesDisponibles(spHabitaciones.getValue());
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
        spHabitaciones.getValueFactory().setValue(0);
        seleccionado = null;
        tablaHoteles.getSelectionModel().clearSelection();
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
