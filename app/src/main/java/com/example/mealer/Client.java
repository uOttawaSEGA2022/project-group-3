package com.example.mealer;

public class Client extends Account {

    Client() {
        this.username = "";
        this.password = "";
    }

    Client(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
