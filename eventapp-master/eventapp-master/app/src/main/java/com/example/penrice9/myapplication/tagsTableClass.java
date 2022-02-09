package com.example.penrice9.myapplication;

/**
 * Created by Penrice9 on 06/02/2019.
 */

public class tagsTableClass {

    private Integer ID, counter, value;
    private String name;

    public tagsTableClass(Integer id, String tag, Integer val, Integer count ){
        this.ID = id;
        this.counter = count;
        this.value = val;
        this.name = tag;
    }

    public Integer getID(){
        return ID;
    }

    public Integer getCounter(){
        return counter;
    }
    public Integer getValue(){
        return value;
    }
    public String getName(){
        return name;
    }

}
