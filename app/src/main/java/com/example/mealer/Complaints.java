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

    public String getComplaintTitle (){return title;}

    public String getComplaintDescription (){return description;}

    public Cook getAssociatedWithComplaint (){return associatedWithComplaint;}

    public String getCookUsername (){return cookUsername;}

    public String getCookID (){return cookID;}

    public void setComplaintTitle (String t){ this.title = t;}

    public void setComplaintDescription (String d){ this.description = d;}

    public void setAssociatedWithComplaint (Cook c){ this.associatedWithComplaint = c;}

    public void setCookUsername (String u){ this.cookUsername = u;}

    public void setCookID (String id){ this.cookID = id;}
}
