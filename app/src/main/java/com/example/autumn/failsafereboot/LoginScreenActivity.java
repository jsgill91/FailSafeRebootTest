package com.example.autumn.failsafereboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Autumn on 10/13/2016.
 *
 * Handles creating a new user and logging in
 */

public class LoginScreenActivity extends AppCompatActivity {
    //this object is passed between LoginScreenActivity and NewUserActivity
    public FailSafe FS_System = new FailSafe();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_activity);
        getSupportActionBar().hide();


        //check if there IS a serializable object to overwrite FS_System with

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            FS_System = (FailSafe) bundle.getSerializable("FailSafe_System");
        }
        //create new user
        final Button newUserBtn = (Button) findViewById(R.id.newUserButton);

        newUserBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent nextScreen = new Intent(v.getContext(), NewUserActivity.class);
                //passing the serializable object
                nextScreen.putExtra("FailSafe_System", FS_System);
                startActivity(nextScreen);
            }
        });

        //login
        final Button loginBtn = (Button) findViewById(R.id.submitButton);
        final EditText usernameField = (EditText) findViewById(R.id.usrnm);
        final EditText passwordField = (EditText) findViewById(R.id.pswd);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();


                if(FS_System.check_credentials(username,password)){
                    Intent nextScreen = new Intent(v.getContext(), MainScreenActivity.class);
                    nextScreen.putExtra("FailSafe_System", FS_System);
                    startActivity(nextScreen);
                }
                else{
                    Toast.makeText(v.getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

    }
}