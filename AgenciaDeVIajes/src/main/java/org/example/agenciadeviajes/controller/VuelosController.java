package org.example.agenciadeviajes.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.VueloDAO;
import org.example.agenciadeviajes.model.Vuelo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

import org.example.agenciadeviajes.dao.ReservaDAO;
import org.example.agenciadeviajes.util.ReservaTemporal;
import org.example.agenciadeviajes.model.DetalleReservaVuelo;

import org.example.agenciadeviajes.model.Reserva;

import org.example.agenciadeviajes.util.Sesion;

import java.math.BigDecimal;

import java.util.List;

public class VuelosController {

    @FXML
    private TableView<Vuelo> tablaVuelos;

    @FXML
    private TableColumn<Vuelo, String> colAerolinea;

    @FXML
    private TableColumn<Vuelo, String> colOrigen;

    @FXML
    private TableColumn<Vuelo, String> colDestino;

    @FXML
    private TableColumn<Vuelo, String> colSalida;

    @FXML
    private TableColumn<Vuelo, String> colLlegada;

    @FXML
    private TableColumn<Vuelo, String> colPrecio;

    @FXML
    private TableColumn<Vuelo, String> colDivisa;

    @FXML
    private TableColumn<Vuelo, Integer> colAsientos;

    @FXML
    private TableColumn<Vuelo, String> colRedondo;

    private final VueloDAO vueloDAO = new VueloDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    @FXML
    public void initialize() {
        try {
            System.out.println("🔄 [VuelosController] Inicializando...");
            configurarTabla();
            cargarVuelos();
            System.out.println("✓ [VuelosController] Inicialización completada");
        } catch (Exception e) {
            System.err.println("❌ [VuelosController.initialize] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTabla() {

        colAerolinea.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAerolinea().getNombre()
                )
        );

        colOrigen.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCiudadOrigen().getNombre()
                )
        );

        colDestino.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCiudadDestino().getNombre()
                )
        );

        colSalida.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFechaSalida().toString()
                )
        );

        colLlegada.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFechaLlegada().toString()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioAsiento()
                )
        );

        colDivisa.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCodigoDivisa()
                )
        );

        colAsientos.setCellValueFactory(
                new PropertyValueFactory<>("asientosDisponibles")
        );

        colRedondo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().isEsRedondo() ? "Sí" : "No"
                )
        );
    }

    private void cargarVuelos() {
        try {
            System.out.println("📋 [cargarVuelos] Obteniendo vuelos de BD...");
            List<Vuelo> vuelos = vueloDAO.obtenerTodos();

            if (vuelos == null) {
                System.err.println("❌ vueloDAO.obtenerTodos() retornó NULL");
                return;
            }

            System.out.println("✓ Vuelos obtenidos: " + vuelos.size());

            if (vuelos.isEmpty()) {
                System.out.println("⚠️ No hay vuelos disponibles en la BD");
            } else {
                for (Vuelo v : vuelos) {
                    System.out.println("  - " + v.toString());
                }
            }

            tablaVuelos.setItems(FXCollections.observableArrayList(vuelos));
            System.out.println("✓ Tabla de vuelos actualizada");
        } catch (Exception e) {
            System.err.println("❌ Error en cargarVuelos: " + e.getMessage());
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

            Stage stage = (Stage) tablaVuelos.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void reservarVuelo() {

        Vuelo vueloSeleccionado =
                tablaVuelos.getSelectionModel().getSelectedItem();

        if (vueloSeleccionado == null) {

            mostrarAlerta(
                    "Selecciona un vuelo."
            );

            return;
        }

        try {

            ReservaTemporal.agregarVuelo(
                    vueloSeleccionado,
                    1
            );

            mostrarExito(
                    "Vuelo agregado al viaje."
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