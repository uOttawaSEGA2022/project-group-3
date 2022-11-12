package com.example.mealer;

public class MenuItem {
    private String title;
    private String description;
    private String ingredients;
    private String cookID;

    public MenuItem(String t, String d, String i,String c){
        this.title=t;
        this.description=d;
        this.ingredients=i;
        this.cookID=c;
    }
    public MenuItem(String t, String d, String i){
        this.title=t;
        this.description=d;
        this.ingredients=i;
    }

    public void setTitle(String t){ this.title=t; }

    public void setDescription (String d){ this.description=d; }

    public void setIngredients (String i){ this.ingredients=i; }

    public void setCookID(String c){ this.cookID=c; }

    public String getTitle(){ return title; }

    public String getDescription() { return description; }

    public String getIngredients() { return ingredients; }

    public String getCookID(){ return cookID; }


}
