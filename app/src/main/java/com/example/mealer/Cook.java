package com.example.mealer;

import android.media.Image;

import java.io.Serializable;

public class Cook extends Account {
    private String firstName;
    private String lastName;
    private String address;
    private String description;
    private Image voidCheque;

    Cook() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.description = "";
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

    public Image getVoidCheque() {
        return voidCheque;
    }

    public void setVoidCheque(Image voidCheque) {
        this.voidCheque = voidCheque;
    }
}
