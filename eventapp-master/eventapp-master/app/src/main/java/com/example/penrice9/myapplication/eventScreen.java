package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.master.glideimageview.GlideImageView;

import java.util.Calendar;

public class eventScreen extends AppCompatActivity {

    public TextView name, location1, location2, location3, location4, date, website;
    public GlideImageView image;
    public Database db;
    public Switch mSwitch;
    public NotificationTimer timer;
    public Boolean change;
    public Notifications n;
    public BottomNavigationView navMenu;
    public Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);
        db = new Database(this);
        name = findViewById(R.id.name);      //Assign XML elements to variable.
        image = findViewById(R.id.image);
        location1 = findViewById(R.id.location1);
        location2 = findViewById(R.id.location2);
        location3 = findViewById(R.id.location3);
        location4 = findViewById(R.id.location4);
        date = findViewById(R.id.date);
        website = findViewById(R.id.website);
        mSwitch = findViewById(R.id.attendSwitch);

        navMenu = findViewById(R.id.navigation);
        context = this;
        navMenu.getMenu().getItem(1).setChecked(true);
        loadData();
        timer = new NotificationTimer(db.getDateOfEvent(date.getText().toString()), this, name.getText().toString(), date.getText().toString());
        final Calendar calendar = Calendar.getInstance();



        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {   //Checks if the switch has been toggled by the user.
                if (isChecked){
                    setNotification(db.changeAttend(getEventName()));
                }else{
                    db.changeAttend(getEventName());
                }
            }
        });

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


    }


    public String getEventName(){   //Function takes message from the intent.
        String message = "";
        if (getIntent().hasExtra("name")){    /*'If' statement prevents the app from crashing
                                                        if there the message is not found.*/
            message = getIntent().getStringExtra("name");
        }
        return message;
    }

    public void loadData(){
        final String [] dataArray = db.getEventData(getEventName());
        name.setText(getEventName()); //Sets display value of the 'TextView'.
        String[] address = dataArray[0].split(",");
        location1.setText(address[0]);
        location2.setText(address[1]);
        location3.setText(address[2]);
        location4.setText(address[3]);
        date.setText(db.formatDate(Integer.parseInt(dataArray[1])));
        website.setText(dataArray[2]);
        website.setMovementMethod(LinkMovementMethod.getInstance());

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(dataArray[2]));
                startActivity(web);
            }
        });


        Glide.with(this).load(dataArray[3]).into(this.image);

        if (db.checkAttend(getEventName())){
            mSwitch.setChecked(true);
        }
        else{
            mSwitch.setChecked(false);
        }
        Toast.makeText(this,String.valueOf(db.getRank(getEventName())), Toast.LENGTH_LONG).show();
    }
    private void setNotification(Boolean notification){
        if (notification && db.notif){
            timer.setTime();
        }
    }

}

