package org.example.agenciadeviajes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.ReservaDAO;

import org.example.agenciadeviajes.model.*;

import org.example.agenciadeviajes.util.ReservaTemporal;

public class ConfirmarReservaController {

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblTotal;

    @FXML
    private ListView<String> listVuelos;

    @FXML
    private ListView<String> listHoteles;

    @FXML
    private ListView<String> listAutos;

    private final ReservaDAO reservaDAO =
            new ReservaDAO();

    private Reserva reserva;

    @FXML
    public void initialize() {

        reserva =
                ReservaTemporal.construirReserva();

        cargarDatos();
    }

    private void cargarDatos() {

        lblTipo.setText(
                "Tipo: " +
                        reserva.getTipoReserva()
        );

        lblTotal.setText(
                "TOTAL: $" +
                        reserva.getTotalPagado() +
                        " " +
                        reserva.getCodigoDivisa()
        );

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
    public void finalizarReserva() {

        try {

            int idReserva =
                    reservaDAO.crearReserva(reserva);

            if (idReserva != -1) {

                mostrarExito(
                        "Reserva creada correctamente.\n" +
                                "Folio: #" + idReserva
                );

                ReservaTemporal.limpiar();

                volverHome();

            } else {

                mostrarAlerta(
                        "No se pudo guardar."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            mostrarAlerta(
                    e.getMessage()
            );
        }
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(

                            getClass().getResource(
                                    "/org/example/agenciadeviajes/view/home.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) lblTipo.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Home");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

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

        alert.setTitle("Reserva Exitosa");

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}