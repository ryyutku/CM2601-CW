module org.example.cw2601v4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.opencsv;


    opens org.example.cw2601v4 to javafx.fxml;
    exports org.example.cw2601v4;
}