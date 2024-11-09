package org.example.cw2601v4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class T {

    private DBConnector dbConnector; // Shared DBConnector instance

    public T(){
        dbConnector = new DBConnector();
    }

    List<Article> articleList = new ArrayList<Article>();

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
                System.out.println(article);
                articleList.add(article);
            }
            System.out.println("Articles fetched and added to articleList");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        T t = new T();
        t.readDB();


    }
}
