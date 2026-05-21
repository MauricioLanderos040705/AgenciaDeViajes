package org.example.agenciadeviajes.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.stage.Stage;

import org.example.agenciadeviajes.dao.AutoDAO;
import org.example.agenciadeviajes.model.Auto;

import java.util.List;

public class AutosController {

    @FXML
    private TableView<Auto> tablaAutos;

    @FXML
    private TableColumn<Auto, String> colMarca;

    @FXML
    private TableColumn<Auto, String> colModelo;

    @FXML
    private TableColumn<Auto, String> colCategoria;

    @FXML
    private TableColumn<Auto, String> colProveedor;

    @FXML
    private TableColumn<Auto, String> colCiudad;

    @FXML
    private TableColumn<Auto, String> colPrecio;

    private final AutoDAO autoDAO = new AutoDAO();

    @FXML
    public void initialize() {

        configurarTabla();

        cargarAutos();
    }

    private void configurarTabla() {

        colMarca.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getMarca()
                )
        );

        colModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getModelo()
                )
        );

        colCategoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getModeloAuto()
                                .getCategoria()
                )
        );

        colProveedor.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getProveedor()
                )
        );

        colCiudad.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue()
                                .getCiudadDisponibilidad()
                                .getNombre()
                )
        );

        colPrecio.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        "$" + cellData.getValue().getPrecioDia()
                                + " " +
                                cellData.getValue().getCodigoDivisa()
                )
        );
    }

    private void cargarAutos() {

        List<Auto> autos = autoDAO.obtenerTodos();

        tablaAutos.setItems(
                FXCollections.observableArrayList(autos)
        );
    }

    @FXML
    public void volverHome() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/agenciadeviajes/view/home.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tablaAutos.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Home");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}