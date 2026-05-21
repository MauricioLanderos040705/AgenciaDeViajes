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

        System.out.println("Abrir vuelos");
    }

    @FXML
    public void abrirHoteles() {

        System.out.println("Abrir hoteles");
    }

    @FXML
    public void abrirAutos() {

        System.out.println("Abrir autos");
    }

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