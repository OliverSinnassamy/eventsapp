package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Home screen of app. List of suggestions and timeline of eventInput. Uses activity_home layout.
 * Values and OnClickListeners added to variables.
 * 'createView()' method assigns adapter and LayoutManager to recyclerView object
 * 'timelineList' method of Database class provides
 * switch-case used to assign methods to buttons.
 */

public class Home extends AppCompatActivity{
    public Database db;
    public Button homeBtn, eventBtn, settingsBtn;
    public BottomNavigationView navMenu;
    public Context context;
    public MenuItem home, browse, settings;
    public final String HOME = "Home", BROWSE = "Browse", SETTINGS = "Settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new Database(this);
        navMenu = findViewById(R.id.navigation);
        this.context = this;


        navMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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



        createTimeline();
        createSuggestions();


    }

    public ArrayList<BrowseElement> events(ArrayList<BrowseElement> eventList){
        ArrayList<BrowseElement> events = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            events.add(eventList.get(i));
        }
        return events;
    }
    public void createTimeline() {
        RecyclerView recyclerView = findViewById(R.id.listView);
        timeline_adapter adapter = new timeline_adapter(db.orderArray(db.timelineList()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createSuggestions(){
        RecyclerView recyclerView = findViewById(R.id.suggestionList);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(events(db.orderArraySuggestions(db.eventList())),this, R.layout.suggestions_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }


}
