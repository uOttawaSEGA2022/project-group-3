package com.example.mealer;

import android.media.Image;

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
}
