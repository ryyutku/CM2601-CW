package com.example.v8;

public class Article {

    private int article_id;
    private String title;
    private String url;
    private String published;
    private String description;
    private String category;





    public Article(int article_id,String title,String url,String published, String description,String category){
        this.article_id = article_id;
        this.title = title;
        this.url = url;
        this.published = published;
        this.description = description;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article [article_id=" + article_id + ", title=" + title ;
    }

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



    public String getDate() {
        return published;
    }

    public void setDate(String date) {
        this.published = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
