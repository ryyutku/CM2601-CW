package com.example.v10;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.math3.linear.SingularMatrixException;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {

//Attributes for the table containing the articles
    public TableView table;
    public TableColumn Category;
    public TableColumn Title;

//Attributes for the table containing the recommended articles
    public TableView Rtable;
    public TableColumn Rtitle;
    public TableColumn Rcategory;


//    Getting the DB connection from the Singleton(Using the same DB connection through out the program)
    DBConnector dbConnector = Singleton.getInstance().getDBConnector();
    Connection connection = dbConnector.getConnection();
//    Getting the class used for interactions between the user and the Database
    UserDA access = new UserDA(dbConnector);
    User user = Singleton.getInstance().getUser();
    ArticleDA articleDA = new ArticleDA();

    MLRecommendation recommender;
    List<Article> recommendedArticles;



    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    // ObservableList for recommended articles
    private ObservableList<Article> recommendedArticleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        Displaying the articlles from the Database in the first table in the gui
        System.out.println("Initialize started");

        try {
            System.out.println("Inside the try method inside the initialize method");
            articleDA.readDB(articleList);
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



//        Populating the second table with the recommended articles for the user
//        populating the data for recommendation
        try {
            // Initialize recommender and get recommended articles
            this.recommender = new MLRecommendation(user.getUserId());
            this.recommendedArticles = recommender.recommendArticles();

            // Populate the ObservableList with recommended articles
            recommendedArticleList.addAll(recommendedArticles);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up the TableView columns to display data
        Rtitle.setCellValueFactory(new PropertyValueFactory<>("title")); // Title column
        Rcategory.setCellValueFactory(new PropertyValueFactory<>("category")); // Category column

        // Set the items (articles) to be displayed in the TableView
        Rtable.setItems(recommendedArticleList);
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


//                For selecting on an article the history is updated
                access.updateHistory(user.getUserId(), articleId);
                System.out.println();
                System.out.println("-------------------------------------------------------------");
                System.out.println("The user id: "+user.getUserId());
                System.out.println("The user is an admin: "+user.isAdmin());
                System.out.println("-------------------------------------------------------------");
                System.out.println();

//                LOADING A NEW SCENE WITH THE ARTICLE CONTENTS
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ArticleContent.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 460, 244);

                ArticleContentController ac = fxmlLoader.getController();
                boolean resize = false;

                if(Singleton.getInstance().getUser().isAdmin()){
                    resize = true;
                }

                // Pass article content to the controller
                ac.setDescriptionText(selectedArticle.getDescription());
                ac.setUrl(selectedArticle.getUrl());
                ac.setArticleId(selectedArticle.getArticle_id());
                ac.setCategory(category);

//            Create a new stage
                Stage newStage = new Stage();
                newStage.setTitle("Article: "+selectedArticle.getTitle());

                newStage.setResizable(resize);
                newStage.setScene(scene);
//              Show the new stage
                newStage.show();

            } else {
                System.out.println("No row selected");
            }
        }
    }


//    Action button for the Administrator if the admin decides to add an article the admin will be directed to corresponding gui
    public void toPublish(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AdministerArticle.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);

            Stage articleStage = new Stage();
            articleStage.setTitle("Admin: "+ Singleton.getInstance().getUser().getUsername());
            articleStage.setScene(scene);
            articleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
