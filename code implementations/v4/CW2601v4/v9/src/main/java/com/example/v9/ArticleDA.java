package com.example.v9;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticleDA {


    DBConnector dbConnector = Singleton.getInstance().getDBConnector();
    Connection connection = dbConnector.getConnection();


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




}

