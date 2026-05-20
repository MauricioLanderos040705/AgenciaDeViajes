module org.example.agenciadeviajes {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.agenciadeviajes to javafx.fxml;
    exports org.example.agenciadeviajes;
}