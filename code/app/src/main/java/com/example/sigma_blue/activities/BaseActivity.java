package com.example.sigma_blue.activities;

import android.content.Intent;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sigma_blue.activities.ActivityLauncher;


/**
 * This is the super class to all the activities in the project. More methods will be added as the
 * project makes progress.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /* This attribute will be used for calling another activity from one.
    * Documentation needed */
    protected final ActivityLauncher<Intent, ActivityResult> activityLauncher
        = ActivityLauncher.registerActivityForResult(this);

    /**
     * This method checks if an edit text is blank or not. Blank means either empty, null, or only
     * white space.
     * @param et This is the edit text box that is being checked if it is empty or not.
     * @return A boolean confirm if et is blank.
     */
    public static boolean isBlank(EditText et) {return et.getText().toString().trim().isEmpty();}
}
