package org.example.cw2601v4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ArticleFetcher implements Initializable {

//    public DBConnector dbConnector ; // Shared DBConnector instance
    private DBConnector dbConnector;

    public TableView<Article> table;
    public TableColumn<Article, String> Category;
    public TableColumn<Article, String> Title;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();


//    public ArticleFetcher(DBConnector dbConnector) {
//        this.dbConnector = dbConnector;
//    }

    public ArticleFetcher(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize started");

        try {
            System.out.println("Inside the try method inside the initialize method");
            readDB();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Display a limited list of articles (up to 20 rows)
        table.setItems(FXCollections.observableArrayList(
                articleList.subList(0, Math.min(20, articleList.size()))
        ));
    }

    public void readDB() throws IOException {
        String query = "SELECT title, category,link,published,description,category,article_id FROM article";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String url = resultSet.getString("link");
                String published = resultSet.getString("published");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                int article_id = Integer.parseInt(resultSet.getString("article_id"));


                // Add data to articleList and data for display and further processing
                Article article = new Article(article_id, title, url, published,description,category);
                articleList.add(article);
            }
            System.out.println("Articles fetched and added to articleList");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void MouseClicked(MouseEvent mouseEvent) throws IOException {
        // Check if the event is a primary mouse click (usually left click)
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // Get the selected item from the table
            Article selectedArticle = (Article) table.getSelectionModel().getSelectedItem();

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
                ac.setUrl(selectedArticle.getUrl());

// Create a new stage (window)
                Stage newStage = new Stage();
                newStage.setTitle("Hello!");
                newStage.setScene(scene);
// Show the new stage
                newStage.show();

            } else {
                System.out.println("No row selected");
            }
        }
    }

}
