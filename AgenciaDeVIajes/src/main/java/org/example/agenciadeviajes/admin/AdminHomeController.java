package org.example.agenciadeviajes.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.example.agenciadeviajes.util.Sesion;

/**
 * Controlador principal del Panel de Administración (versión simplificada)
 */
public class AdminHomeController {

    @FXML private BorderPane root;
    @FXML private ListView<String> menuAdmin;
    @FXML private Label lblUsuario;

    @FXML
    public void initialize() {
        if (!Sesion.esAdmin()) {
            alert("Acceso Denegado", "Solo administradores");
            return;
        }

        lblUsuario.setText("👤 " + Sesion.getNombreActual());

        menuAdmin.getItems().addAll(
                "Hoteles",
                "Vuelos",
                "Aerolíneas",
                "Autos",
                "Ciudades",
                "Países",
                "Divisas",
                "Usuarios"
        );

        menuAdmin.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) cargarModulo(nuevo);
        });
    }

    private void cargarModulo(String opcion) {
        try {
            String fxml = switch (opcion) {
                case "Hoteles" -> "/org/example/agenciadeviajes/views/admin/hotel-admin.fxml";
                case "Vuelos" -> "/org/example/agenciadeviajes/views/admin/vuelo-admin.fxml";
                case "Aerolíneas" -> "/org/example/agenciadeviajes/views/admin/aerolinea-admin.fxml";
                case "Autos" -> "/org/example/agenciadeviajes/views/admin/auto-admin.fxml";
                case "Ciudades" -> "/org/example/agenciadeviajes/views/admin/ciudad-admin.fxml";
                case "Países" -> "/org/example/agenciadeviajes/views/admin/pais-admin.fxml";
                case "Divisas" -> "/org/example/agenciadeviajes/views/admin/divisa-admin.fxml";
                case "Usuarios" -> "/org/example/agenciadeviajes/views/admin/usuario-admin.fxml";
                default -> null;
            };

            if (fxml != null) {
                Parent vista = FXMLLoader.load(getClass().getResource(fxml));
                root.setCenter(vista);
            }
        } catch (Exception e) {
            alert("Error", "No se pudo cargar el módulo");
        }
    }

    private void alert(String titulo, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
