package org.example.agenciadeviajes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.example.agenciadeviajes.model.Usuario;
import org.example.agenciadeviajes.util.Sesion;

public class HomeController {

    @FXML
    private Label lblBienvenida;

    @FXML
    public void initialize() {

        if (Sesion.haySesion()) {

            lblBienvenida.setText(
                    "Bienvenido(a), " +
                            Sesion.getUsuarioActual().getNombreCompleto()
            );
        }
    }

    @FXML
    public void abrirVuelos() {

        try {

            FXMLLoader loader = new FXMLLoader(

                    getClass().getResource("/org/example/agenciadeviajes/view/vuelos.fxml")

            );

            Parent root = loader.load();

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Vuelos");

        } catch (Exception e) {

            e.printStackTrace();

        }    }

    @FXML
    public void abrirHoteles() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/hoteles.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Hoteles");

        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @FXML
    public void abrirAutos() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/autos.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Autos");

        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @FXML
    public void abrirReservas() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/example/agenciadeviajes/view/mis_reservas.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage =
                    (Stage) lblBienvenida.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Mis Reservas");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion() {

        try {

            Sesion.cerrarSesion();

            FXMLLoader loader = new FXMLLoader(

                    getClass().getResource("/org/example/agenciadeviajes/view/login.fxml")

            );

            Parent root = loader.load();

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Login");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}