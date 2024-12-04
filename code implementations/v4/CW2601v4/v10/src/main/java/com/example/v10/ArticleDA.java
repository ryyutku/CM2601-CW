package com.example.v10;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//Handles interactions between the articles and the Database
public class ArticleDA {


    DBConnector dbConnector = Singleton.getInstance().getDBConnector();
    Connection connection = dbConnector.getConnection();

//Insert an article to the database
    public void insertArticle(String title, String link, String published, String description, String category) {
        String query = "INSERT INTO article (title, link, published, description, category) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the value of the placeholder
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, link);
            preparedStatement.setString(3, published);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, category);

            preparedStatement.executeUpdate();
            System.out.println("Article inserted successfully!");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Connection is null. Check DBConnector setup.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    Read an article from the Databaes
    public void readDB(ObservableList<Article> articleList ) throws IOException {
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





}

