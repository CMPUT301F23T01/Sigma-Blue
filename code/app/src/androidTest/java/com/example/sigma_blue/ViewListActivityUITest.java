package com.example.sigma_blue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewListActivityUITest {
    @Rule
    public ActivityScenarioRule<ViewListActivity> scenario = new
            ActivityScenarioRule<ViewListActivity>(ViewListActivity.class);

    @After
    public void tearDown() {
        ;
    }
}
