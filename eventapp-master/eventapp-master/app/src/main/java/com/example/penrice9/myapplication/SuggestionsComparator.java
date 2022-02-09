package com.example.penrice9.myapplication;

import java.util.Comparator;

/**
 * Created by Penrice9 on 03/01/2019.
 */

public class SuggestionsComparator implements Comparator<BrowseElement>{

    @Override
    public int compare(BrowseElement o1, BrowseElement o2) {
        if (o1.getRating() == o2.getRating())
            return 0;
        if (o1.getRating() < o2.getRating())
            return 1;
        return -1;
    }
}
