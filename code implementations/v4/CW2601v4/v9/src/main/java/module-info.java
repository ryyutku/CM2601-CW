module com.example.v {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires java.sql;
    requires java.desktop;


    opens com.example.v9 to javafx.fxml;
    exports com.example.v9;
}