package com.example.androidfinalproject;

public class Article {
    private String title;
    private String body;
    private String link;
    private String imageLink;
    private int articleID;


    public Article(String title, String body, String link, String imageLink, int articleID){
        this.title = title;
        this.body = body;
        this.link=link;
        this.imageLink=imageLink;
        this.articleID = articleID;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {return link;}

    public String getImageLink() {return imageLink;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLink(String link) {this.link=link;}

    public void setImageLink(String imageLink) {this.imageLink = imageLink;}

    public int getNewsID() {
        return articleID;
    }

    public void setNewsID(int newsID) {
        this.articleID = newsID;
    }
}