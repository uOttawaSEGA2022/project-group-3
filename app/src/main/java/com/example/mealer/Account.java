package com.example.mealer;

import java.io.Serializable;

public abstract class Account implements Serializable {
    protected String username;
    protected String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
