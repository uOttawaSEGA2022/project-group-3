package com.example.mealer;

public class MenuItem {
    private String title;
    private String description;
    private String ingredients;
    private String cookID;
    private boolean isOffered;

    public MenuItem(String t, String d, String i,String c, boolean b){
        this.title=t;
        this.description=d;
        this.ingredients=i;
        this.cookID=c;
        this.isOffered=b;
    }
    public MenuItem(String t, String d, String i,boolean b){
        this.title=t;
        this.description=d;
        this.ingredients=i;
        this.isOffered=b;
    }

    public void setTitle(String t){ this.title=t; }

    public void setDescription (String d){ this.description=d; }

    public void setIngredients (String i){ this.ingredients=i; }

    public void setCookID(String c){ this.cookID=c; }

    public void setIsOffered (boolean b){ this.isOffered =b; }

    public String getTitle(){ return title; }

    public String getDescription() { return description; }

    public String getIngredients() { return ingredients; }

    public String getCookID(){ return cookID; }

    public boolean getIsOffered(){ return isOffered; }


}
