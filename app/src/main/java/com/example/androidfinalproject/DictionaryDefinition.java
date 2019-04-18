package com.example.androidfinalproject;
/**
 * This class sets the attributes of DictionaryDefinition objects
 * @author Raphael Guerra
 * 16-04-2019
 **/
public class DictionaryDefinition{
    private int id;
    private String title;
    private String definition;
    /**
     * Initial Consrtructor
     **/
    public DictionaryDefinition(int i, String t, String d){
        id = i;
        title = t;
        definition = d;
    }
    public DictionaryDefinition(){
        id = -1;
        title = null;
        definition = null;
    }
    /**
     * getters and setters
     **/
    public String getTitle(){return title;}
    public void setTitle(String T){title = T;}

    public String getDefinition(){return definition;}
    public void setDefinition(String D){definition = D;}

    public int getId(){return id;}
    public void setId(int i){id = i;}
}