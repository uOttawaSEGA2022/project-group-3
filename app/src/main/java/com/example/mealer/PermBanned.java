package com.example.mealer;

public class PermBanned implements Bannable{
    private boolean isBanned;

    public PermBanned (){
        this.isBanned=false;
    }

    public void setIsBanned (boolean a){
        this.isBanned=a;
    }

    public boolean isPermBanned(){
        return isBanned;
    }

    public boolean isTempBanned(){
        return false;
    }




}
