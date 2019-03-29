package com.example.androidfinalproject;

public class NewsFeed {

    private String newsTitle;
    private String newsBody;
    private int newsId;

    public NewsFeed() {

    }

    public NewsFeed(int newsId, String newsTitle, String newsBody) {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.newsBody= newsBody;
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
