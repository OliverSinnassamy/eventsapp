package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginScreen extends AppCompatActivity {
    Database myData;
    EditText username, password;
    Button submitBtn;
    TextView click;
    Context context;
    Database.updateEventTable eventUpdate;
    Database.updatetagTable tagUpdate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submitBtn = findViewById(R.id.submitBtn);
        click = findViewById(R.id.clickHere);
        context = this;


        myData = new Database(this);
        eventUpdate = new Database.updateEventTable();
        tagUpdate = new Database.updatetagTable();

        eventUpdate.execute();
        tagUpdate.execute();

        Toast.makeText(this, myData.passwords().get(myData.passwords().size()-1),Toast.LENGTH_LONG).show();





        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "OnClick method");
                if (!username.getText().toString().equals("")){
               boolean login = myData.fetchDetails(username.getText().toString(), password.getText().toString(), context);
               Log.d("Debug:", "Data fetched");
               if (login){
                   Log.d("Debug:", "Before new page");
                   myData.setUsername(username.getText().toString());
                   newPage(username.getText().toString(), Home.class);
               }}
               else{
                    Toast.makeText(context, "Please enter username", Toast.LENGTH_LONG).show();
                }

            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPage(username.getText().toString(), RegisterScreen.class);
            }
        });
    }

    public void stuff() {
        boolean start = myData.fetchDetails(username.getText().toString(), password.getText().toString() /*fetches details from database*/, this);
        if (start) {
            newPage(username.getText().toString(), Home.class); //Loads new screen
        }
    }

    public void newPage(String message, Class activity){
        //adds extra information in this case, username to be accessed in other class
        Intent intent = new Intent(this,activity);
        intent.putExtra("name", message);
        //method to launch new activity using the intent
        startActivity(intent);
    }

}
