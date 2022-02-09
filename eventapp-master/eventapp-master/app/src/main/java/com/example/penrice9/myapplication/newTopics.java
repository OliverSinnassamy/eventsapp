package com.example.penrice9.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

public class newTopics extends AppCompatActivity implements View.OnClickListener {
    public CardView sportCard, musicCard, comedyCard, otherCard;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topics);

        sportCard = findViewById(R.id.sportCard);
        musicCard = findViewById(R.id.musicCard);
        comedyCard= findViewById(R.id.comedyCard);
        otherCard = findViewById(R.id.otherCard);

        sportCard.setOnClickListener(this);
        musicCard.setOnClickListener(this);
        comedyCard.setOnClickListener(this);
        otherCard.setOnClickListener(this);
        Log.d("", String.valueOf(sportCard.getId()));

        db = new Database(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sportCard:
                db.newTopics("sport");
                break;

            case R.id.musicCard:
                db.newTopics("music");
                break;

            case R.id.comedyCard:
                db.newTopics("comedy");
                break;

            case R.id.otherCard:
                db.newTopics("other");
                break;

        }
        newPage("", Home.class);
    }

    public void newPage(String message, Class activity){
        Intent intent = new Intent(this,activity);
        intent.putExtra("name", message);
        startActivity(intent);
    }
}