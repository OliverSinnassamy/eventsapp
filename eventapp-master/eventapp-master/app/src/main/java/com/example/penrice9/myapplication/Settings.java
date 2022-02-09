package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    EditText password, passwordConf;
    public Switch notif;
    Button submit;
    Database db;
    Context context;
    public BottomNavigationView navMenu;
    public static Boolean notifCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        password = findViewById(R.id.newPassword);
        passwordConf = findViewById(R.id.newPasswordConf);
        submit = findViewById(R.id.submitBtn);
        notif = findViewById(R.id.notifications);
        context = this;
        navMenu = (findViewById(R.id.navigation));

        navMenu.getMenu().getItem(2).setChecked(true);

        db = new Database(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().length() <3 || !passwordConf.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(context,"Please enter password and confirmation again, passwords must be at least 3 characters", Toast.LENGTH_LONG).show();
                }
                else{
                    if (db.settingsFunction(password.getText().toString())) {
                        Toast.makeText(context, "Password Updated", Toast.LENGTH_LONG).show();
                    }}
            }
        });

        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.notificationCheck(notif);
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
}
