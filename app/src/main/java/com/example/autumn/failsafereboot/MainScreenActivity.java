package com.example.autumn.failsafereboot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Autumn on 10/14/2016.
 */
public class MainScreenActivity extends AppCompatActivity {
    public FailSafe FS_System = new FailSafe();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FS_System = (FailSafe) bundle.getSerializable("FailSafe_System");
        }

        final Button addBtn = (Button) findViewById(R.id.addClassBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(v.getContext(), AddClassActivity.class);
                //passing the serializable object
                nextScreen.putExtra("FailSafe_System", FS_System);
                startActivity(nextScreen);
            }
        });
    }

}
