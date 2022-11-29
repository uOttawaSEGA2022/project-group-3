package com.example.mealer;

import java.util.Calendar;

public class Complaints {
    String title;
    String description;
    Cook associatedWithComplaint;

    String cookUsername;
    String cookID;

    public Complaints (String title, String description, Cook a){
        this.title=title;
        this.description=description;
        this.associatedWithComplaint=a;
    }

    public Complaints (String title, String description, String cookUsername, String cookID){
        this.title = title;
        this.description = description;
        this.cookUsername = cookUsername;
        this.cookID = cookID;
    }




}
