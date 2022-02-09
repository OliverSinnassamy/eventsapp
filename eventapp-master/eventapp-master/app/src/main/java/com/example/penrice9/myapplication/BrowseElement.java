package com.example.penrice9.myapplication;

/**
 * Created by Penrice9 on 03/01/2019.
 */

public class BrowseElement {
    private String name, image;
    private float rating;

    public BrowseElement(String givenName, String givenImage, float givenRating){
        name = givenName;
        image = givenImage;
        rating = givenRating;
    }

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public float getRating(){
        return rating;
    }
}
