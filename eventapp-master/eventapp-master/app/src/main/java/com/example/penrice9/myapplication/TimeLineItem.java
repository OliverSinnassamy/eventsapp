package com.example.penrice9.myapplication;

/**
 * Created by Penrice9 on 20/12/2018.
 */

public class TimeLineItem {

    private String name;
    private Integer date;

    public TimeLineItem(String givenName, Integer givenDate){
        name = givenName;
        date = givenDate;
    }

    public String getName(){
        return name;
    }

    public Integer getDate(){
        return date;
    }

}
