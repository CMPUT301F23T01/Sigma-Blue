package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;

public class UITestTools {
    public void closeKeyboard() {
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
    }
}
