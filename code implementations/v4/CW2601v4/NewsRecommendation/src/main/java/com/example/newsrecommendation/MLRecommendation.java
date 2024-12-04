package com.example.newsrecommendation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MLRecommendation {

    Connection connection = Singleton.getInstance().getDBConnector().getConnection();


    private List<UserPreference> userPreferences;
    private List<Article> allArticles;

    public MLRecommendation(int userId) throws SQLException {
        this.userPreferences = getUserPreferences(userId);
        this.allArticles = getAllArticles();
    }


    // Method to recommend articles for a user
    public List<Article> recommendArticles() {
        // Create a TF-IDF vector for user preferences based on category scores
        Map<String, Double> userVector = new HashMap<>();
        for (UserPreference preference : userPreferences) {
            userVector.put(preference.category, preference.pscore);
        }

        // Calculate similarity for each article and add it to the articles list
        for (Article article : allArticles) {
            article.similarityScore = calculateCosineSimilarity(userVector, article.getTfIdfVector());
        }

        // Sort articles by similarity score (highest first)
        List<Article> sortedArticles = allArticles.stream()
                .sorted((a, b) -> Double.compare(b.similarityScore, a.similarityScore))
                .collect(Collectors.toList());

        // Randomize the recommended articles list to add diversity
        Collections.shuffle(sortedArticles);

        // Return the top 5 (after randomization)
        return sortedArticles.subList(0, Math.min(sortedArticles.size(), 5));
    }

    // Fetch user preferences from the database
    private List<UserPreference> getUserPreferences(int userId) {
        List<UserPreference> preferences = new ArrayList<>();
        String query = "SELECT category, pscore FROM upreferences WHERE userId = ?";

        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String category = rs.getString("category");
                double pscore = rs.getDouble("pscore");
                preferences.add(new UserPreference(category, pscore));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preferences;
    }

    // Fetch all articles from the database
    private List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT title, category, description FROM article";

        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String category = rs.getString("category");
                String description = rs.getString("description");

                Article article = new Article(title, category, description);
                article.generateTfIdfVector(); // Generate TF-IDF vector for the article
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    // Calculate cosine similarity between two vectors
    private double calculateCosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        RealVector v1 = mapToVector(vec1);
        RealVector v2 = mapToVector(vec2);

        try {
            return v1.dotProduct(v2) / (v1.getNorm() * v2.getNorm());
        } catch (DimensionMismatchException e) {
            return 0.0; // Handle mismatched dimensions gracefully
        }
    }

    // Convert map to RealVector for similarity calculation
    private RealVector mapToVector(Map<String, Double> vectorMap) {
        double[] values = vectorMap.values().stream().mapToDouble(Double::doubleValue).toArray();
        return new ArrayRealVector(values);
    }

    // Main method to test the system
    public static void main(String[] args) throws SQLException {
        int userId = 5; // Example userId

        MLRecommendation recommender = new MLRecommendation(userId);
        List<Article> recommendedArticles = recommender.recommendArticles();

        System.out.println("Recommended Articles:");
        for (Article article : recommendedArticles) {
            System.out.println(article.getTitle() + " (" + article.getCategory() + ") - Score: " + article.similarityScore);
        }
    }

}