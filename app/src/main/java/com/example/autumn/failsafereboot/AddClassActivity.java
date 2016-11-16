package com.example.autumn.failsafereboot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Autumn on 10/18/2016.
 */
public class AddClassActivity extends FragmentActivity {
    public FailSafe FS_System;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_class_activity);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FS_System = (FailSafe) bundle.getSerializable("FailSafe_System");
        }

        final EditText cNameField = (EditText) findViewById(R.id.cName);
        final EditText cIdField = (EditText) findViewById(R.id.cID);

        final EditText testWeightField = (EditText) findViewById(R.id.testWeightBox);
        final EditText testDropField = (EditText) findViewById(R.id.testDropBox);
        final EditText quizWeightField = (EditText) findViewById(R.id.quizWeightBox);
        final EditText quizDropField = (EditText) findViewById(R.id.quizDrop);
        final EditText hwWeightField = (EditText) findViewById(R.id.hwWeightBox);
        final EditText hwDropField = (EditText) findViewById(R.id.hwDrop);
        final EditText labWeightField = (EditText) findViewById(R.id.labWeightBox);
        final EditText labDropField = (EditText) findViewById(R.id.labDrop);
        final EditText icaWeightField = (EditText) findViewById(R.id.icaWeightBox);
        final EditText icaDropField = (EditText) findViewById(R.id.icaDrop);
        final EditText projectWeightField = (EditText) findViewById(R.id.projectWeightBox);
        final EditText projectDropField = (EditText) findViewById(R.id.projectDrop);

        final Button submitButton = (Button) findViewById(R.id.classSubmitBtn);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Gradebook newClass = new Gradebook(context);
                String weightString;
                String dropString;
                double weight;

                /*
                    it occurs to me, i need to provide a check box for students to indicate whether they have the assignment on the list of assignments
                    OR just have the database ignore them somehow. I literally forgot where that thought was going
                 */

                newClass.setCourseName(cNameField.getText().toString());
                newClass.setCourseID(cIdField.getText().toString());

                weightString = testWeightField.getText().toString();
                dropString = testDropField.getText().toString();
                if(weightString != ""){
                    weight = Double.valueOf(testWeightField.getText().toString())/100.0;
                    newClass.set_weight(0,weight);
                }
                if(dropString != ""){
                    newClass.set_drop(0,Integer.valueOf(dropString));
                }


                weight = Double.valueOf(hwWeightField.getText().toString())/100.0;
                newClass.set_weight(1,weight);
                newClass.set_drop(1,Integer.valueOf(hwDropField.getText().toString()));

                weight = Double.valueOf(labWeightField.getText().toString())/100.0;
                newClass.set_weight(2,weight);
                newClass.set_drop(2,Integer.valueOf(labDropField.getText().toString()));

                weight = Double.valueOf(icaWeightField.getText().toString())/100.0;
                newClass.set_weight(3,weight);
                newClass.set_drop(3,Integer.valueOf(icaDropField.getText().toString()));

                weight = Double.valueOf(projectWeightField.getText().toString())/100.0;
                newClass.set_weight(4,weight);
                newClass.set_drop(4,Integer.valueOf(projectDropField.getText().toString()));

                weight = Double.valueOf(quizWeightField.getText().toString())/100.0;
                newClass.set_weight(5,weight);
                newClass.set_drop(5,Integer.valueOf(quizDropField.getText().toString()));

                FS_System.add_class(newClass);
            }
        });

    }


}
