module org.example.agenciadeviajes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.agenciadeviajes to javafx.fxml;
    exports org.example.agenciadeviajes;
}