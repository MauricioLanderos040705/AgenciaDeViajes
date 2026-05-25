package org.example.agenciadeviajes.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.agenciadeviajes.util.Sesion;

/**
 * Controlador principal del Panel de Administración
 */
public class AdminHomeController {

    @FXML private Label lblUsuario;

    @FXML
    public void initialize() {
        if (!Sesion.esAdmin()) {
            alert("Acceso Denegado", "Solo administradores");
            return;
        }

        lblUsuario.setText("👤 " + Sesion.getNombreActual());
    }

    @FXML
    private void abrirHoteles() {
        cargarModulo("Hoteles");
    }

    @FXML
    private void abrirVuelos() {
        cargarModulo("Vuelos");
    }

    @FXML
    private void abrirAerolineas() {
        cargarModulo("Aerolíneas");
    }

    @FXML
    private void abrirAutos() {
        cargarModulo("Autos");
    }

    @FXML
    private void abrirCiudades() {
        cargarModulo("Ciudades");
    }

    @FXML
    private void abrirPaises() {
        cargarModulo("Países");
    }

    @FXML
    private void abrirDivisas() {
        cargarModulo("Divisas");
    }

    @FXML
    private void abrirUsuarios() {
        cargarModulo("Usuarios");
    }

    @FXML
    private void cerrarSesion() {
        try {
            Sesion.cerrarSesion();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/login.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 470, 530));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarModulo(String opcion) {
        try {
            String fxml = switch (opcion) {
                case "Hoteles" -> "/org/example/agenciadeviajes/view/hotel-admin.fxml";
                case "Vuelos" -> "/org/example/agenciadeviajes/view/vuelo-admin.fxml";
                case "Aerolíneas" -> "/org/example/agenciadeviajes/view/aerolinea-admin.fxml";
                case "Autos" -> "/org/example/agenciadeviajes/view/auto-admin.fxml";
                case "Ciudades" -> "/org/example/agenciadeviajes/view/ciudad-admin.fxml";
                case "Países" -> "/org/example/agenciadeviajes/view/pais-admin.fxml";
                case "Divisas" -> "/org/example/agenciadeviajes/view/divisa-admin.fxml";
                case "Usuarios" -> "/org/example/agenciadeviajes/view/usuario-admin.fxml";
                default -> null;
            };

            if (fxml != null) {
                Parent vista = FXMLLoader.load(getClass().getResource(fxml));
                Stage stage = (Stage) lblUsuario.getScene().getWindow();
                stage.setScene(new Scene(vista, 900, 600));
                stage.setTitle("Administración - " + opcion);
            }
        } catch (Exception e) {
            alert("Error", "No se pudo cargar el módulo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
