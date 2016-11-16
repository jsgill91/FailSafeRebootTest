package com.example.autumn.failsafereboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Autumn on 10/14/2016.
 */
public class NewUserActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_activity);
        getSupportActionBar().hide();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final FailSafe FS_System = (FailSafe) bundle.getSerializable("FailSafe_System");
        /*
        I'm sleepy. Need to figure out how to create a new User object that's
        permanent within FailSafe and update its username and password.
         */
        final Button submitBtn = (Button) findViewById(R.id.submitButton);
        final EditText usernameField = (EditText) findViewById(R.id.usrnm);
        final EditText passwordField = (EditText) findViewById(R.id.pswd);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                FS_System.set_username(username);
                FS_System.set_password(password);

                Intent nextScreen = new Intent(v.getContext(), LoginScreenActivity.class);
                nextScreen.putExtra("FailSafe_System", FS_System);
                startActivity(nextScreen);
            }
        });
    }
}
