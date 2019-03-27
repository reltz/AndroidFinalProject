package com.example.androidfinalproject;

public class SearchActivity {

    private String search;
    private boolean isClicked;
    private int id;

    public SearchActivity() {

    }

    public SearchActivity(int id, String search, boolean isClicked) {
        this.search = search;
        this.id = id;
        this.isClicked = isClicked;
    }

    public String getSearch() {
        return search;
    }

    public int getID() {
        return id;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setSend(boolean click) {
        isClicked = click;
    }
}