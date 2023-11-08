package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewListActivityUITest {
    @Rule
    public ActivityScenarioRule<ViewListActivity> scenario = new
            ActivityScenarioRule<ViewListActivity>(ViewListActivity.class);

    /**
     * As an owner, I want to add an item to my items, with a date of purchase or acquisition, brief
     * description, make, model, serial number (if applicable), estimated value, and comment.
     */
    @Test
    public void add_item_US_01_01_01() {
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.button_edit)).perform(click());
        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("iName"));
        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
    }
    @After
    public void tearDown() {
        ;
    }
}
