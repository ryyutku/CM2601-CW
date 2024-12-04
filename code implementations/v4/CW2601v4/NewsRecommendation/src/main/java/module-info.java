module com.example.news {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires commons.math3;


    opens com.example.newsrecommendation to javafx.fxml;
    exports com.example.newsrecommendation;
}