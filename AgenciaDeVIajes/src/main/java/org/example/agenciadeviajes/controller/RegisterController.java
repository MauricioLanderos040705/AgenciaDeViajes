package org.example.agenciadeviajes.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.UsuarioDAO;
import org.example.agenciadeviajes.model.Usuario;
import org.example.agenciadeviajes.util.PasswordUtil;

public class RegisterController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void registrarUsuario() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText().trim();

        if (nombre.isEmpty() ||
                apellido.isEmpty() ||
                correo.isEmpty() ||
                password.isEmpty()) {

            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Completa todos los campos.");
            return;
        }

        if (usuarioDAO.correoExiste(correo)) {

            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Ese correo ya está registrado.");
            return;
        }

        // HASH AUTOMÁTICO
        String hash = PasswordUtil.hashSHA256(password);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreo(correo);
        usuario.setContrasenia(hash);
        // NUEVO: Establecer rol por defecto a 'CLIENTE'
        usuario.setRol("CLIENTE");

        boolean registrado = usuarioDAO.insertar(usuario);

        if (registrado) {

            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setText("Usuario registrado correctamente. Inicia sesión.");

            limpiarCampos();

        } else {

            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error al registrar usuario.");
        }
    }

    @FXML
    public void volverLogin() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) txtNombre.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {

        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtPassword.clear();
    }
}
