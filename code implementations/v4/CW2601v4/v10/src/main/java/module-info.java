module com.example.v {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires commons.math3;


    opens com.example.v10 to javafx.fxml;
    exports com.example.v10;
}