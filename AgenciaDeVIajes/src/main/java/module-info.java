module org.example.agenciadeviajes {

    requires javafx.controls;

    requires javafx.fxml;

    requires java.sql;
    requires com.github.librepdf.openpdf;
    requires java.desktop;


    opens org.example.agenciadeviajes to javafx.fxml;

    opens org.example.agenciadeviajes.model to javafx.base;

    opens org.example.agenciadeviajes.controller to javafx.fxml;
    opens org.example.agenciadeviajes.admin to javafx.fxml;

    exports org.example.agenciadeviajes;
    exports org.example.agenciadeviajes.controller;
    exports org.example.agenciadeviajes.admin;

    opens org.example.agenciadeviajes.factory to javafx.base;

}