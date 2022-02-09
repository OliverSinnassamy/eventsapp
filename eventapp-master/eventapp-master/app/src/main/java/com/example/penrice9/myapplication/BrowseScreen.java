package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * First iteration:
 * No pictures
 * Large gaps between items
 * No colour or styling
 * Image sources hardcoded not retrieved from database
 */
public class BrowseScreen extends AppCompatActivity {
    private ArrayList<BrowseElement> events = new ArrayList<>();
    public Database db;
    public BottomNavigationView navMenu;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        db = new Database(this);
        navMenu = (findViewById(R.id.navigation));
        this.context = this;
        //getArray();
        Toast.makeText(this, db.getImages().get(2),Toast.LENGTH_LONG).show();
        getImages();
        navMenu.getMenu().getItem(1).setChecked(true);

        //Called when one of the items is clicked.
        navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //uses the name of the item as a way of differentiating between them
                switch(String.valueOf(item.getTitle())){
                    case "Home" :
                        Intent intentToHome = new Intent(context, Home.class);
                        startActivity(intentToHome);
                        break;

                    case "Browse":
                        Intent intentToEvents = new Intent(context, BrowseScreen.class);
                        startActivity(intentToEvents);
                        break;

                    case "Settings":
                        Intent intentToSettings = new Intent(context, Settings.class);
                        startActivity(intentToSettings);
                        break;
                }

                return false;
            }
        });

    }

    public void getImages() {
        events=db.eventList();
        createView();
    }

    public void createView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(db.orderArraySuggestions(events),this, R.layout.card_layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


    }

}
