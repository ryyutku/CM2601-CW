module com.example.v {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.v8 to javafx.fxml;
    exports com.example.v8;
}