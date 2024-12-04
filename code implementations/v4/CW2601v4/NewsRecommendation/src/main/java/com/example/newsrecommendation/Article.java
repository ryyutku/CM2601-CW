package com.example.newsrecommendation;

import java.util.HashMap;
import java.util.Map;

//The article class
public class Article {

    private int article_id;
    private String title;
    private String url;
    private String published;
    private String description;
    private String category;




//    constructor for the Article class
    public Article(int article_id,String title,String url,String published, String description,String category){
        this.article_id = article_id;
        this.title = title;
        this.url = url;
        this.published = published;
        this.description = description;
        this.category = category;
    }


//    Setters and getters for the attributes
    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String content) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getPublished() {
        return published;
    }

    public void setPublished(String date) {
        this.published = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


//    Attributes for the Machine Learning Recommendation
    double similarityScore; // To store similarity score
    private Map<String, Double> tfIdfVector; //  vector representation

//    Method Overloading
//    Used when using the Machine Learning Recommendation class
    public Article(String title, String category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.similarityScore = 0.0;
    }

    public void generateTfIdfVector() {
//         Generate a simple vector based on the category
        tfIdfVector = new HashMap<>();
        tfIdfVector.put(category, 1.0); // Assign a score of 1.0 for the category
    }

    public Map<String, Double> getTfIdfVector() {
        return tfIdfVector;
    }

}
