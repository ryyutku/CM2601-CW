package com.example.v10;

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

//        Checks if the fields in the gui is empty or not

//        Creating a BooleanBind to check if any field is empty
        BooleanBinding allFieldsFilled = category.textProperty().isEmpty()
                .or(date.textProperty().isEmpty())
                .or(description.textProperty().isEmpty())
                .or(link.textProperty().isEmpty())
                .or(title.textProperty().isEmpty());

//        Bind the disable property of the publish button to the BooleanBinding
        publish.disableProperty().bind(allFieldsFilled);
    }


//    Loading the Articles1 gui
    public void ReadArticles(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Articles1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);

            Stage articleStage = new Stage();
            articleStage.setTitle("Articles");
            articleStage.setScene(scene);
            articleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Method to publish a new article
    public void PublishArticle(ActionEvent actionEvent) {

//        Creating a new article
        ArticleDA insert = new ArticleDA();
        insert.insertArticle( title.getText(), link.getText(),date.getText(),  description.getText(),category.getText());

//        resetting all the fields in the gui
        setToBlank();

        System.out.println("Article Published!");
    }

//    Method to set the fields in the gui to blank
    public void setToBlank(){
        category.setText("");
        date.setText("");
        description.setText("");
        link.setText("");
        title.setText("");

    }
}
