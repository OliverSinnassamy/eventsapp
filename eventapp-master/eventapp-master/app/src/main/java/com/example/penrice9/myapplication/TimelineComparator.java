package com.example.penrice9.myapplication;

import java.util.Comparator;

/**
 * Created by Penrice9 on 20/12/2018.
 */

public class TimelineComparator implements Comparator<TimeLineItem> {
    public TimelineComparator() {

    }

    @Override
    //method that compares the two items
    public int compare(TimeLineItem t1, TimeLineItem t2) {
        //compares date attribute of object to the other
        if (t1.getDate() == t2.getDate())
            return 0;
        //returns -1 if t1 is smaller than t2
        if (t1.getDate() < t2.getDate())
            return -1;
        return 1;
    }
}

