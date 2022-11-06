package com.example.mealer;

import java.io.Serializable;

public class Account implements Serializable {
    protected String username;
    protected String password;
    protected String role;
    protected String id;
    protected Bannable banStatus;

    public void setBanStatus(Bannable a){this.banStatus=a;}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() { return role; }

    public void setRole(String role) {
        this.role = role;
    }
}
