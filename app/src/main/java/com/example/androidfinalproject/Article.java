package com.example.androidfinalproject;

public class Article {private String title;
    private String body;
    private int articleID;


    public Article(String title, String body, int articleID){
        this.title = title;
        this.body = body;
        this.articleID = articleID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getNewsID() {
        return articleID;
    }

    public void setNewsID(int newsID) {
        this.articleID = newsID;
    }
}