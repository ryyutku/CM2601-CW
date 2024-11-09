package org.example.cw2601v4;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ArticleContentController {


    String articleUrl;
    public TextArea Content;

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



    @FXML
    private Button url;




//    public void OnURLclick(javafx.event.ActionEvent actionEvent) {
//        String articleUrl = "http://techcrunch.com/2020/09/07/vodafone-idea-rebrand-vi/";  // Replace with the actual URL
//
//        try {
//            URI uri = new URI(articleUrl);
//            if (Desktop.isDesktopSupported()) {
//                Desktop.getDesktop().browse(uri);
//            } else {
//                System.out.println("Desktop is not supported on this platform.");
//            }
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    public void setUrl(String url) {
        this.articleUrl = url;
    }

    public void OnURLclick(javafx.event.ActionEvent actionEvent) {

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

}
