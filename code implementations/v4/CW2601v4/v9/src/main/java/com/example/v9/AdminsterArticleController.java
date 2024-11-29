package com.example.v9;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminsterArticleController {

    @FXML
    private TextField category;

    @FXML
    private TextField date;

    @FXML
    private TextArea description;

    @FXML
    private TextField link;

    @FXML
    private Button publish;

    @FXML
    private TextField title;

    @FXML
    public void initialize() {

        // Create a BooleanBinding to check if any field is empty
        BooleanBinding allFieldsFilled = category.textProperty().isEmpty()
                .or(date.textProperty().isEmpty())
                .or(description.textProperty().isEmpty())
                .or(link.textProperty().isEmpty())
                .or(title.textProperty().isEmpty());

        // Bind the disable property of the publish button to the BooleanBinding
        publish.disableProperty().bind(allFieldsFilled);
    }

    private void checkFields() {
        // Enable the publish button only if all fields are filled
        boolean allFieldsFilled = !category.getText().isEmpty()
                && !date.getText().isEmpty()
                && !description.getText().isEmpty()
                && !link.getText().isEmpty()
                && !title.getText().isEmpty();

        publish.setDisable(!allFieldsFilled);
    }

    public void ReadArticles(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Articles1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);

            Stage articleStage = new Stage();
            articleStage.setTitle("Articles");
            articleStage.setScene(scene);
            articleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PublishArticle(ActionEvent actionEvent) {

//        Creating a new article
        ArticleDA insert = new ArticleDA();
        insert.insertArticle(category.getText(), date.getText(), description.getText(), link.getText(), title.getText());

        setToBlank();

        System.out.println("Article Published!");
    }

    public void setToBlank(){
        category.setText("");
        date.setText("");
        description.setText("");
        link.setText("");
        title.setText("");

    }
}
