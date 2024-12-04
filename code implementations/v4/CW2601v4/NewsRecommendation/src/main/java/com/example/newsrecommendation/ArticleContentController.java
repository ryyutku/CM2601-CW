package com.example.newsrecommendation;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class ArticleContentController {


    public int articleId;
    public String articleUrl;
    public TextArea Content;
    public String category;

//    Using the singleton to get the current users instance
    User user = Singleton.getInstance().getUser();
    UserDA access = new UserDA(new DBConnector());

    @FXML
    private ComboBox<Integer> ratingComboBox;


    //
////    Can use this class to do update (USER_PREFERENCE), (USER_HISTORY), (RATED), (LIKED), (SKIPPED ARTICLES)
//

    public void initialize() {
        System.out.println("This is the Article Content Controller class");

        // Initialize ComboBox with ratings 1 to 5
        ratingComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        // Set an action listener to handle rating selection
        ratingComboBox.setOnAction(event -> {
            try {
                handleRatingSelection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    Method to handle any actions related to rating
    private void handleRatingSelection() throws SQLException {
        int rating = ratingComboBox.getValue(); // Get the selected rating (1 to 5)
         // Get the current article ID

        // Save the rating to the database
        saveRatingToDatabase(user.getUserId(), articleId, rating);
        access.updatePreference(user.getUserId(),category,rating/10);
    }

//    Method to save the ratings data to the database
    private void saveRatingToDatabase(int userId,int articleId, int rating) throws SQLException {
        access.updateRatings(rating,userId,articleId);
    }

//    Method to save the Description of the article to give a context of the article to user
    public void setDescriptionText(String content) {
        Content.setText(content);
        Content.setEditable(false);
        Content.setWrapText(true);
    }

//    Method to set the articleId
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setCategory(String category) {this.category = category;}



    @FXML
    private Button url;

//    Method to set the Url to be redirected site of the article
    public void setUrl(String url) {
        this.articleUrl = url;
    }

//    Method to handle any action taken place if the user decide to read the article by clicking the link
    public void OnURLclick(ActionEvent actionEvent) {

        try {
            URI uri = new URI(articleUrl);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            } else {
                System.out.println("Desktop is not supported on this platform.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

//Methods to handle interactions between the user and the article (like,skip)
    public void onLikeClick(ActionEvent actionEvent) throws SQLException {
        access.updateInteraction("like", user.getUserId(), articleId);
        access.updatePreference(user.getUserId(), category,0.2);

    }

    public void onSkipClick(ActionEvent actionEvent) throws SQLException {
        access.updateInteraction("skip",1,articleId);
        access.updatePreference(user.getUserId(), category,-0.2);


    }
}
