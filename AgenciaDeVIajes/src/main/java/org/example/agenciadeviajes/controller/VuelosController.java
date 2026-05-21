package org.example.agenciadeviajes.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.VueloDAO;
import org.example.agenciadeviajes.model.Vuelo;

import java.util.List;

public class VuelosController {

    @FXML
    private TableView<Vuelo> tablaVuelos;

    @FXML
    private TableColumn<Vuelo, String> colAerolinea;

    @FXML
    private TableColumn<Vuelo, String> colOrigen;

    @FXML
    private TableColumn<Vuelo, String> colDestino;

    @FXML
    private TableColumn<Vuelo, String> colSalida;

    @FXML
    private TableColumn<Vuelo, String> colPrecio;

    @FXML
    private TableColumn<Vuelo, Integer> colAsientos;

    private final VueloDAO vueloDAO = new VueloDAO();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarVuelos();
    }

    private void configurarTabla() {

        colAerolinea.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAerolinea().getNombre()
                )
        );

        colOrigen.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCiudadOrigen().getNombre()
                )
        );

        colDestino.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCiudadDestino().getNombre()
                )
        );

        colSalida.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getFechaSalida().toString()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioAsiento()
                )
        );

        colAsientos.setCellValueFactory(
                new PropertyValueFactory<>("asientosDisponibles")
        );
    }

    private void cargarVuelos() {

        List<Vuelo> vuelos = vueloDAO.obtenerTodos();

        tablaVuelos.setItems(
                FXCollections.observableArrayList(vuelos)
        );
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tablaVuelos.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}