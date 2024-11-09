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

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArticleController1 implements Initializable {


    public TableView table;
    public TableColumn Category;
    public TableColumn Title;
    List<String[]> data = new ArrayList<>();

    DBConnector dbConnector = new DBConnector();


    private ObservableList<Article> articleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize started");

        try {
            System.out.println("Inside the try method inside the initialize method");
            readDB();
//            readCSV();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        createList();

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
    public void readCSV() throws IOException {
        System.out.println("Inside the readCSV method");
        String file = "src/main/resources/article.csv";
        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
//                 Use a regular expression to split on commas not inside quotes
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                System.out.println(line);
                data.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
        System.out.println("Finished reading the csv");
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




    public void createList() {
        System.out.println("Inside the createList method");

        for (int i = 1; i < data.size(); i++) {
            System.out.println("Title: "+data.get(i)[1]);
            System.out.println("category: "+data.get(i)[0]);

//            int article_id = Integer.parseInt(data.get(i)[0]);

            String title = data.get(i)[0];
            String url = data.get(i)[1];
            String date = data.get(i)[2];
            String content = data.get(i)[3];
            String category = data.get(i)[4];
            int article_id = Integer.parseInt(data.get(i)[5]);

            Article article = new Article(article_id,title,url,date,content,category);

            articleList.add(article);
            System.out.println();
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

    public void visitSite(String url) {
        String articleUrl = url;  // Replace with the actual URL

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
