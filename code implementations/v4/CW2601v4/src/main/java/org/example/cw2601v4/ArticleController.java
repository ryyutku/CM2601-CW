package org.example.cw2601v4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {


    @FXML
    private TableColumn<Article, String> Author;
    @FXML
    private TableColumn<Article, String> Category;
    @FXML
    private TableColumn<Article, String> Publish_date;
    @FXML
    private TableColumn<Article, String> Title;
    @FXML
    private TableView<Article> table;



    List<String[]> data = new ArrayList<>();

    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize started");

        try {
            System.out.println("Inside the try method inside the initialize method");
            readCSV();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createList();

        System.out.println("Inserting the data inside the Table starting with Title");
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Author.setCellValueFactory(new PropertyValueFactory<>("author"));
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        Publish_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(articleList);
    }

    public void readCSV() throws IOException {
        System.out.println("Inside the readCSV method");
        String file = "src/main/resources/articles.csv";
        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                // Use a regular expression to split on commas not inside quotes
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
    }

//    CREATING LIST FOR THE "articles.csv"
    public void createList() {
        System.out.println("Inside the createList method");

        for (int i = 1; i < data.size(); i++) {
            int article_id = Integer.parseInt(data.get(i)[0]);
            String title = data.get(i)[1];
            String content = data.get(i)[2];
            String category = data.get(i)[3];
            String author = data.get(i)[4];
            String date = data.get(i)[5];
            Article article = new Article(article_id,title,content, category, author, date);

            articleList.add(article);
        }
    }

   
    
    public void MouseClicked(MouseEvent mouseEvent) throws IOException {
        // Check if the event is a primary mouse click (usually left click)
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // Get the selected item from the table
            Article selectedArticle = table.getSelectionModel().getSelectedItem();

            // Check if an item was selected
            if (selectedArticle != null) {
                // Output the selected article details to the label or console
                System.out.println("Selected Article: " + selectedArticle.getTitle());

//                String content = selectedArticle.getContent();
//                ******************************************************
//                LOADING A NEW SCENE WITH THE ARTICLE CONTENTS
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ArticleContent.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 826.0, 463.0);

                ArticleContentController ac = fxmlLoader.getController();

                // Pass article content to the controller
                ac.setContentText(selectedArticle.getDescription());
// Create a new stage (window)
                Stage newStage = new Stage();
                newStage.setTitle("Hello!");
                newStage.setScene(scene);
// Show the new stage
                newStage.show();
//                ******************************************************


//                CAN CREATE AN ARTICLE_CONTENT_CONTROLLER INSTANCE AND PASS THE ARTICLE CONTENT OR ID FROM HERE

            } else {
                System.out.println("No row selected");
            }
        }
    }






    public void loadArticleContent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ArticleContent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
// Create a new stage (window)
        Stage newStage = new Stage();
        newStage.setTitle("Hello!");
        newStage.setScene(scene);
// Show the new stage
        newStage.show();
    }
//
//




}




