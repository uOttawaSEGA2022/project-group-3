package com.example.mealer;

public class MenuItem {
    private String title;
    private String description;
    private String ingredients;
    private String mealType;
    private String cuisineType;
    private String allergens;
    private String price;
    private String cookID;
    private boolean isOffered;

    public MenuItem(String t, String d, String i, String m, String cu, String a, String p, String c, boolean b) {
        this.title=t;
        this.description=d;
        this.ingredients=i;
        this.cookID=c;
        this.mealType = m;
        this.cuisineType = cu;
        this.allergens = a;
        this.price = p;
        this.isOffered=b;
    }

    public MenuItem(String t, String d, String i,String c, boolean b){
        this.title=t;
        this.description=d;
        this.ingredients=i;
        this.cookID=c;
        this.isOffered=b;
    }

    public void setTitle(String t){ this.title=t; }

    public void setDescription (String d){ this.description=d; }

    public void setIngredients (String i){ this.ingredients=i; }

    public void setMealType(String mealType) { this.mealType = mealType; }

    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public void setAllergens(String allergens) { this.allergens = allergens; }

    public void setPrice(String price) { this.price = price; }

    public void setCookID(String c){ this.cookID=c; }

    public void setIsOffered (boolean b){ this.isOffered =b; }

    public String getTitle(){ return title; }

    public String getDescription() { return description; }

    public String getIngredients() { return ingredients; }

    public String getCookID(){ return cookID; }

    public String getMealType() { return mealType; }

    public String getCuisineType() { return cuisineType; }

    public String getAllergens() { return allergens; }

    public String getPrice() { return price; }

    public boolean getIsOffered(){ return isOffered; }


}
