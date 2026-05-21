package org.example.agenciadeviajes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.example.agenciadeviajes.model.Usuario;

public class HomeController {

    @FXML
    private Label lblBienvenida;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {

        this.usuario = usuario;

        lblBienvenida.setText(
                "Bienvenido, " + usuario.getNombreCompleto()
        );
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

        System.out.println("Abrir reservas");
    }

    @FXML
    public void cerrarSesion() {

        try {

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