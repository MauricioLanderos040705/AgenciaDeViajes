package org.example.agenciadeviajes.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import org.example.agenciadeviajes.util.PDFReservaGenerator;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.ReservaDAO;

import org.example.agenciadeviajes.model.DetalleReservaAuto;
import org.example.agenciadeviajes.model.DetalleReservaHotel;
import org.example.agenciadeviajes.model.DetalleReservaVuelo;
import org.example.agenciadeviajes.model.Reserva;

import org.example.agenciadeviajes.util.Sesion;

import java.util.List;

public class MisReservasController {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colFolio;

    @FXML
    private TableColumn<Reserva, String> colFecha;

    @FXML
    private TableColumn<Reserva, String> colTipo;

    @FXML
    private TableColumn<Reserva, String> colTotal;

    @FXML
    private ListView<String> listVuelos;

    @FXML
    private ListView<String> listHoteles;

    @FXML
    private ListView<String> listAutos;

    private final ReservaDAO reservaDAO =
            new ReservaDAO();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarReservas();

        tablaReservas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, reserva) -> {

                    if (reserva != null) {

                        mostrarDetalles(reserva);
                    }
                });
    }

    private void configurarTabla() {

        colFolio.setCellValueFactory(cellData ->

                new javafx.beans.property.SimpleStringProperty(

                        cellData.getValue().getFolio()
                )
        );

        colFecha.setCellValueFactory(cellData ->

                new javafx.beans.property.SimpleStringProperty(

                        cellData.getValue()
                                .getFechaReserva()
                                .toString()
                )
        );

        colTipo.setCellValueFactory(cellData ->

                new javafx.beans.property.SimpleStringProperty(

                        cellData.getValue()
                                .getTipoReserva()
                )
        );

        colTotal.setCellValueFactory(cellData ->

                new javafx.beans.property.SimpleStringProperty(

                        "$" +
                                cellData.getValue().getTotalPagado() +
                                " " +
                                cellData.getValue().getCodigoDivisa()
                )
        );
    }

    private void cargarReservas() {

        List<Reserva> reservas =
                reservaDAO.obtenerPorUsuario(

                        Sesion.getUsuarioActual().getIdUsuario()
                );

        tablaReservas.setItems(

                FXCollections.observableArrayList(reservas)
        );
    }

    private void mostrarDetalles(Reserva reserva) {

        listVuelos.getItems().clear();
        listHoteles.getItems().clear();
        listAutos.getItems().clear();

        for (DetalleReservaVuelo dv :
                reserva.getDetallesVuelo()) {

            listVuelos.getItems().add(
                    dv.toString()
            );
        }

        for (DetalleReservaHotel dh :
                reserva.getDetallesHotel()) {

            listHoteles.getItems().add(
                    dh.toString()
            );
        }

        for (DetalleReservaAuto da :
                reserva.getDetallesAuto()) {

            listAutos.getItems().add(
                    da.toString()
            );
        }
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(

                    getClass().getResource(
                            "/org/example/agenciadeviajes/view/home.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage =
                    (Stage) tablaReservas.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Home");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    @FXML
    public void generarPDF() {

        Reserva reservaSeleccionada =
                tablaReservas.getSelectionModel().getSelectedItem();

        if (reservaSeleccionada == null) {

            mostrarAlerta(
                    "Selecciona una reserva."
            );

            return;
        }

        try {

            PDFReservaGenerator.generarPDF(
                    reservaSeleccionada
            );

            mostrarExito(
                    "Ticket PDF generado correctamente."
            );

        } catch (Exception e) {

            e.printStackTrace();

            mostrarAlerta(
                    "Error al generar PDF."
            );
        }
    }
    //Metodos y alartas de exito y error
    private void mostrarAlerta(String mensaje) {

        Alert alert =
                new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Aviso");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
    private void mostrarExito(String mensaje) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("PDF Generado");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}