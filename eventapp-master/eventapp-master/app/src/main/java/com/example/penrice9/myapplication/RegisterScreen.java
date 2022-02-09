package com.example.penrice9.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterScreen extends AppCompatActivity {

    EditText username, password, confirm;
    Button register;
    Database db;
    Context context;
    String TAG = "RegisterScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        username = findViewById(R.id.newUsername);
        password = findViewById(R.id.newPassword);
        confirm = findViewById(R.id.newPassConf);
        register = findViewById(R.id.registerBtn);
        this.context = this;
        db = new Database(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().length() <3 || username.getText().toString().length() >15 ) {
                    Toast.makeText(context, "Username must be 3-15 characters long", Toast.LENGTH_LONG).show();
                }if (username.getText().toString().matches("[0-9]+")){
                    Toast.makeText(context, "Username must contain at least 1 alphabetical character", Toast.LENGTH_LONG).show();
                }else{
                    if(db.findUsername(username.getText().toString())) {
                    }else{
                        Toast.makeText(context, "Username taken, please enter new username", Toast.LENGTH_LONG).show();
                    }
                    if (password.getText().toString().length() <6){
                        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                    }
                        if (password.getText().toString().equals(confirm.getText().toString())) {
                            db.insertUserData(username.getText().toString(), password.getText().toString());
                            Toast.makeText(context, "Details added", Toast.LENGTH_LONG).show();
                            newPage(username.getText().toString(), newTopics.class);

                        } else {
                            Toast.makeText(context, "Passwords do not match, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
            }
        });
    }
    public void newPage(String message, Class activity){
        Intent intent = new Intent(this,activity);
        intent.putExtra("name", message);
        startActivity(intent);
    }


}
