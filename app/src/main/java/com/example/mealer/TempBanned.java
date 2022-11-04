package com.example.mealer;

public class TempBanned implements Bannable{
    private boolean isBanned;

    public TempBanned(){
        isBanned=false;
    }

    public void setIsBanned (boolean a){
        this.isBanned=a;
    }

    public boolean isPermBanned(){
        return false;
    }

    public boolean isTempBanned(){
        return isBanned;
    }

}
