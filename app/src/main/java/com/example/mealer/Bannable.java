package com.example.mealer;

import java.util.Calendar;

public interface Bannable {
    
    boolean isBanned = false;

    public default void setIsBanned(boolean a) {}

    public default boolean getIsBanned(){ return isBanned; }




}
