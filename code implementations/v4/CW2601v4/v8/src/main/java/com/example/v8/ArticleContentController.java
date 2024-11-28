package com.example.v8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    User user = Singleton.getInstance().getUser();


    UserDA access = new UserDA(new DBConnector());

    //
////    Can use this class to do update (USER_PREFERENCE), (USER_HISTORY), (RATED), (LIKED), (SKIPPED ARTICLES)
//
    public ArticleContentController() throws IOException {
        System.out.println("This is the Article Content Controller class");

    }

    public void setContentText(String content) {
        Content.setText(content);
        Content.setEditable(false); // Optional: Make it non-editable if it's for display only
        Content.setWrapText(true);
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }



    @FXML
    private Button url;





    public void setUrl(String url) {
        this.articleUrl = url;
    }

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

//    ArticleId and UserId should be passed on the two methods

    public void onLikeClick(ActionEvent actionEvent) throws SQLException {
        access.updateInteraction("like", user.getUserId(), articleId);
        access.updatePreference(user.getUserId(), "business",2);

    }

    public void onSkipClick(ActionEvent actionEvent) throws SQLException {
        access.updateInteraction("skip",1,articleId);
        access.updatePreference(1,"business",-2);


    }
}
