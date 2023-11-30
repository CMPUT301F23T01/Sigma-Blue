package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Checks.checkNotNull;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.EasyMock2Matchers.equalTo;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

public class UITestTools {
    public void closeKeyboard() {
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
    }

    public static void signup(String username, String password) {
        onView(withId(R.id.createAccButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions
                .typeText(username));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions
                .typeText(password));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("CONFIRM"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }
    public static void login(String username, String password) {
        // login with new account
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText(username));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText(password));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("LOGIN"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }


    // methods from https://developer.android.com/training/testing/espresso/lists

    public static Matcher<Object> withItemContent(String expectedText) {
        checkNotNull(expectedText);
        return withItemContent(equalTo(expectedText));
    }

}
