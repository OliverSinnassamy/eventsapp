package com.example.penrice9.myapplication;

/**
 * Created by Penrice9 on 03/11/2018.
 */

public class Event {

    public int id, date, attend;
   public Float rating;
    public String name, location, website, tags, image;

    public Event(int id, String name, int date, String location, String website, float rating, int attend, String tags, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.rating = rating;
        this.tags = tags;
        this.attend = attend;
        this.website = website;
        this.image = image;
    }


}
