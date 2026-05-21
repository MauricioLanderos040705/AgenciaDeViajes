package org.example.agenciadeviajes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.agenciadeviajes.util.Sesion;
import org.example.agenciadeviajes.dao.UsuarioDAO;
import org.example.agenciadeviajes.model.Usuario;
import org.example.agenciadeviajes.util.PasswordUtil;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void iniciarSesion() {

        String correo = txtCorreo.getText().trim();

        String password = txtPassword.getText().trim();

        if (correo.isEmpty() || password.isEmpty()) {

            lblMensaje.setText("Completa todos los campos.");

            return;

        }

        String hash = PasswordUtil.hashSHA256(password);

        Usuario usuario = usuarioDAO.validarLogin(correo, hash);

        if (usuario != null) {

            // GUARDAR SESIÓN GLOBAL

            Sesion.iniciarSesion(usuario);

            lblMensaje.setStyle("-fx-text-fill: green;");

            lblMensaje.setText("Bienvenido " + usuario.getNombre());

            abrirHome(usuario);

        } else {

            lblMensaje.setStyle("-fx-text-fill: red;");

            lblMensaje.setText("Correo o contraseña incorrectos.");

        }

    }

    private void abrirHome(Usuario usuario) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) txtCorreo.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void abrirRegistro() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) txtCorreo.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Registro");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}