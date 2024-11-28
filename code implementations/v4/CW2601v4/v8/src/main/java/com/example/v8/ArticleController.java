package com.example.v8;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {


    public TableView table;
    public TableColumn Category;
    public TableColumn Title;

    DBConnector dbConnector = Singleton.getInstance().getDBConnector();
    Connection connection = dbConnector.getConnection();
    UserDA access = new UserDA(dbConnector);
    User user = Singleton.getInstance().getUser();



    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize started");

        try {
            System.out.println("Inside the try method inside the initialize method");
            readDB();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Inserting the data inside the Table starting with Title");
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Category.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Set a sublist to limit rows
        ObservableList<Article> limitedList = FXCollections.observableArrayList(
                articleList.subList(0, Math.min(20, articleList.size()))
        );

        table.setItems(limitedList); // Use the limited list

        table.setItems(articleList);
    }

    public void readDB() throws IOException {
        String query = "SELECT title, category,link,published,description,category,articleId FROM article";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String url = resultSet.getString("link");
                String published = resultSet.getString("published");
                String description = resultSet.getString("description");
                String category = resultSet.getString("category");
                int article_id = Integer.parseInt(resultSet.getString("articleId"));


                // Add data to articleList and data for display and further processing
                Article article = new Article(article_id, title, url, published,description,category);
                articleList.add(article);
            }
            System.out.println("Articles fetched and added to articleList");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void MouseClicked(MouseEvent mouseEvent) throws IOException, SQLException {
        // Check if the event is a primary mouse click (usually left click)
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // Get the selected item from the table
            Article selectedArticle = (Article) table.getSelectionModel().getSelectedItem();

            // Check if an item was selected
            if (selectedArticle != null) {

                // Output the selected article details to the label or console
                System.out.println("Selected Article: " + selectedArticle.getTitle());

//                -----------------GETTING THE SELECTED ARTICLE CATEGORY-----------------------
                int articleId = selectedArticle.getArticle_id();
                String category = selectedArticle.getCategory();
                System.out.println("Category: " + category);


//                For selecting on an article the preference score is updated
                access.updatePreference(user.getUserId(), category,2);
                access.updateHistory(user.getUserId(), articleId);
                System.out.println();
                System.out.println("-------------------------------------------------------------");
                System.out.println("The user id: "+user.getUserId());
                System.out.println("The user is an admin: "+user.isAdmin());
                System.out.println("-------------------------------------------------------------");
                System.out.println();


//                -----------------------------------------------------------------------------

//                String content = selectedArticle.getContent();
//                ******************************************************
//                LOADING A NEW SCENE WITH THE ARTICLE CONTENTS
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ArticleContent.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);

                ArticleContentController ac = fxmlLoader.getController();

                // Pass article content to the controller
                ac.setContentText(selectedArticle.getDescription());
                ac.setUrl(selectedArticle.getUrl());
                ac.setArticleId(selectedArticle.getArticle_id());

// Create a new stage (window)
                Stage newStage = new Stage();
                newStage.setTitle(selectedArticle.getArticle_id()+""+selectedArticle.getTitle());
                newStage.setScene(scene);
// Show the new stage
                newStage.show();

            } else {
                System.out.println("No row selected");
            }
        }
    }




}
