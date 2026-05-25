package org.example.agenciadeviajes.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.AutoDAO;
import org.example.agenciadeviajes.model.Auto;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import org.example.agenciadeviajes.dao.ReservaDAO;

import org.example.agenciadeviajes.model.DetalleReservaAuto;
import org.example.agenciadeviajes.model.Reserva;

import org.example.agenciadeviajes.util.ReservaTemporal;
import org.example.agenciadeviajes.util.Sesion;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

public class AutosController {

    @FXML
    private TableView<Auto> tablaAutos;
    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private TableColumn<Auto, String> colMarca;

    @FXML
    private TableColumn<Auto, String> colModelo;

    @FXML
    private TableColumn<Auto, String> colCategoria;

    @FXML
    private TableColumn<Auto, String> colProveedor;

    @FXML
    private TableColumn<Auto, String> colCiudad;

    @FXML
    private TableColumn<Auto, String> colPrecio;

    private final AutoDAO autoDAO = new AutoDAO();
    private final ReservaDAO reservaDAO = new ReservaDAO();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarAutos();
    }

    private void configurarTabla() {

        colMarca.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getMarca()
                )
        );

        colModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getModelo()
                )
        );

        colCategoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getCategoria()
                )
        );

        colProveedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getProveedor()
                )
        );

        colCiudad.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getCiudadDisponibilidad()
                                .getNombre()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioDia()
                                + " " +
                                cellData.getValue().getCodigoDivisa()
                )
        );
    }

    private void cargarAutos() {

        List<Auto> autos = autoDAO.obtenerTodos();

        tablaAutos.setItems(
                FXCollections.observableArrayList(autos)
        );
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tablaAutos.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void reservarAuto() {

        Auto autoSeleccionado =
                tablaAutos.getSelectionModel().getSelectedItem();

        if (autoSeleccionado == null) {

            mostrarAlerta(
                    "Selecciona un auto."
            );

            return;
        }

        LocalDate fechaInicio =
                dpFechaInicio.getValue();

        LocalDate fechaFin =
                dpFechaFin.getValue();

        if (fechaInicio == null || fechaFin == null) {

            mostrarAlerta(
                    "Selecciona fechas."
            );

            return;
        }

        if (!fechaFin.isAfter(fechaInicio)) {

            mostrarAlerta(
                    "Fecha fin inválida."
            );

            return;
        }

        try {

            ReservaTemporal.agregarAuto(
                    autoSeleccionado,
                    fechaInicio,
                    fechaFin
            );

            mostrarExito(
                    "Auto agregado al viaje."
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