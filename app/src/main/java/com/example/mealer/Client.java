package com.example.mealer;

public class Client extends Account {
    private String firstName;
    private String lastName;
    private String address;
    private String creditCardInfo;

    Client() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.creditCardInfo = "";
    }

    Client(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
