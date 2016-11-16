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
                    newClass.set_drop(5,Integer.valueOf(quizDropField.getText().toString()));

                FS_System.add_class(newClass);
            }
        });

    }


}
