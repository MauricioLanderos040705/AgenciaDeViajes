package org.example.agenciadeviajes.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.HotelDAO;
import org.example.agenciadeviajes.model.Hotel;

import java.util.List;

public class HotelesController {

    @FXML
    private TableView<Hotel> tablaHoteles;

    @FXML
    private TableColumn<Hotel, String> colNombre;

    @FXML
    private TableColumn<Hotel, String> colCiudad;

    @FXML
    private TableColumn<Hotel, String> colPais;

    @FXML
    private TableColumn<Hotel, String> colEstrellas;

    @FXML
    private TableColumn<Hotel, String> colPrecio;

    @FXML
    private TableColumn<Hotel, Integer> colHabitaciones;

    private final HotelDAO hotelDAO = new HotelDAO();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarHoteles();
    }

    private void configurarTabla() {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCiudad.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCiudad().getNombre()
                )
        );

        colPais.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getCiudad()
                                .getPais()
                                .getNombre()
                )
        );

        colEstrellas.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getEstrellasStr()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioNoche()
                                + " " +
                                cellData.getValue().getCodigoDivisa()
                )
        );

        colHabitaciones.setCellValueFactory(
                new PropertyValueFactory<>("habitacionesDisponibles")
        );
    }

    private void cargarHoteles() {

        List<Hotel> hoteles = hotelDAO.obtenerTodos();

        tablaHoteles.setItems(
                FXCollections.observableArrayList(hoteles)
        );
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tablaHoteles.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}