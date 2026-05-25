module org.example.agenciadeviajes {

    requires javafx.controls;

    requires javafx.fxml;

    requires java.sql;
    requires com.github.librepdf.openpdf;
    requires java.desktop;


    opens org.example.agenciadeviajes to javafx.fxml;

    opens org.example.agenciadeviajes.model to javafx.base;

    opens org.example.agenciadeviajes.controller to javafx.fxml;

    exports org.example.agenciadeviajes;

    exports org.example.agenciadeviajes.controller;
    opens org.example.agenciadeviajes.factory to javafx.base;

}