package com.example.mealer;

public class Client extends Account {
    private String firstName;
    private String lastName;
    private String address;
    private String creditCardNumber;

    Client() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.creditCardNumber = "";
    }

    Client(String username, String password) {
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

    public String getCreditCardNumber(){ return creditCardNumber;}

    public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber;}
}
