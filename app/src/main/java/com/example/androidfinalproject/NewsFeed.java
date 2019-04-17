package com.example.androidfinalproject;

public class NewsFeed {

    private String newsTitle, newsBody, URL, imageURL;
    private int newsId;

    public NewsFeed() {

    }

    public NewsFeed(int newsId, String newsTitle, String newsBody, String URL, String imageURL) {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.newsBody= newsBody;
        this.imageURL = imageURL;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
