package org.example.agenciadeviajes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.agenciadeviajes.util.PasswordUtil;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource(
                        "/org/example/agenciadeviajes/view/login.fxml"
                )
        );

        Scene scene = new Scene(fxmlLoader.load(), 400, 350);

        stage.setTitle("Agencia de Viajes");
        stage.setScene(scene);
        stage.show();
        System.out.println(

                    //Prueba para el hash de la contraseña en lab BD
                PasswordUtil.hashSHA256("1234")

        );
    }

    public static void main(String[] args) {
        launch();


    }
}