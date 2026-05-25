package org.example.agenciadeviajes.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.HotelDAO;
import org.example.agenciadeviajes.model.Hotel;
import javafx.scene.control.Alert;

import javafx.scene.control.DatePicker;

import javafx.scene.control.Spinner;

import javafx.scene.control.SpinnerValueFactory;

import org.example.agenciadeviajes.dao.ReservaDAO;

import org.example.agenciadeviajes.model.DetalleReservaHotel;

import org.example.agenciadeviajes.model.Reserva;

import org.example.agenciadeviajes.util.ReservaTemporal;
import org.example.agenciadeviajes.util.Sesion;

import java.math.BigDecimal;

import java.time.LocalDate;

import java.util.List;

public class HotelesController {

    @FXML
    private TableView<Hotel> tablaHoteles;
    @FXML
    private DatePicker dpCheckIn;

    @FXML
    private DatePicker dpCheckOut;

    @FXML
    private Spinner<Integer> spHabitaciones;

    @FXML
    private TableColumn<Hotel, String> colNombre;

    @FXML
    private TableColumn<Hotel, String> colCiudad;

    @FXML
    private TableColumn<Hotel, String> colPais;

    @FXML
    private TableColumn<Hotel, String> colEstrellas;

    @FXML
    private TableColumn<Hotel, String> colPrecio;

    @FXML
    private TableColumn<Hotel, Integer> colHabitaciones;

    private final HotelDAO hotelDAO = new HotelDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    @FXML
    public void initialize() {
        try {
            System.out.println("🔄 [HotelesController] Inicializando...");
            configurarTabla();
            spHabitaciones.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(
                            1,
                            10,
                            1
                    )
            );
            cargarHoteles();
            System.out.println("✓ [HotelesController] Inicialización completada");
        } catch (Exception e) {
            System.err.println("❌ [HotelesController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCiudad.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCiudad().getNombre()
                )
        );

        colPais.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getCiudad()
                                .getPais()
                                .getNombre()
                )
        );

        colEstrellas.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getEstrellasStr()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioNoche()
                                + " " +
                                cellData.getValue().getCodigoDivisa()
                )
        );

        colHabitaciones.setCellValueFactory(
                new PropertyValueFactory<>("habitacionesDisponibles")
        );
    }

    private void cargarHoteles() {
        try {
            System.out.println("📋 [cargarHoteles] Obteniendo hoteles de BD...");
            List<Hotel> hoteles = hotelDAO.obtenerTodos();

            if (hoteles == null) {
                System.err.println("❌ hotelDAO.obtenerTodos() retornó NULL");
                return;
            }

            System.out.println("✓ Hoteles obtenidos: " + hoteles.size());

            if (hoteles.isEmpty()) {
                System.out.println("⚠️ No hay hoteles disponibles en la BD");
            } else {
                for (Hotel h : hoteles) {
                    System.out.println("  - " + h.toString());
                }
            }

            tablaHoteles.setItems(FXCollections.observableArrayList(hoteles));
            System.out.println("✓ Tabla de hoteles actualizada");
        } catch (Exception e) {
            System.err.println("❌ Error en cargarHoteles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tablaHoteles.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void reservarHotel() {

        Hotel hotelSeleccionado =
                tablaHoteles.getSelectionModel().getSelectedItem();

        if (hotelSeleccionado == null) {

            mostrarAlerta(
                    "Selecciona un hotel."
            );

            return;
        }

        LocalDate checkIn = dpCheckIn.getValue();
        LocalDate checkOut = dpCheckOut.getValue();

        if (checkIn == null || checkOut == null) {

            mostrarAlerta(
                    "Selecciona fechas."
            );

            return;
        }

        if (!checkOut.isAfter(checkIn)) {

            mostrarAlerta(
                    "Check-Out debe ser posterior."
            );

            return;
        }

        try {

            ReservaTemporal.agregarHotel(
                    hotelSeleccionado,
                    checkIn,
                    checkOut,
                    spHabitaciones.getValue()
            );

            mostrarExito(
                    "Hotel agregado al viaje."
            );

        } catch (Exception e) {

            e.printStackTrace();

            mostrarAlerta(
                    e.getMessage()
            );
        }
    }
    private void mostrarAlerta(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Aviso");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
    private void mostrarExito(String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Reserva Exitosa");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}