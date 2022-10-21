package com.example.mealer;

import android.graphics.Bitmap;
import android.media.Image;

public class Cook extends Account {
    private String firstName;
    private String lastName;
    private String address;
    private String description;
    private String id;
    private Bitmap voidCheque;

    Cook() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.description = "";
        this.id = "";
    }

    Cook(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getVoidCheque() {
        return voidCheque;
    }

    public void setVoidCheque(Bitmap voidCheque) {
        this.voidCheque = voidCheque;
    }

}
