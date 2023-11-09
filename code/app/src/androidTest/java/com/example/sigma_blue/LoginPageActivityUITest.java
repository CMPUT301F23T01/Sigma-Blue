package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.util.Checks.checkNotNull;


import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginPageActivityUITest {
    @Rule
    public ActivityScenarioRule<LoginPageActivity> scenario = new
            ActivityScenarioRule<LoginPageActivity>(LoginPageActivity.class);

    /**
     * As a user, I want a profile with a unique username.
     * Check if a invalid account gets denied
     */
    @Test
    public void check_login_US_06_01_01() {
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText("Temp_User"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("password"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("LOGIN"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        // shouldn't be able to login with a bad account
        onView(withText("Login")).check(matches(isDisplayed()));
        onView(withText("Incorrect Username or Password")).check(matches(isDisplayed()));
    }
    /**
     * As a user, I want a profile with a unique username.
     * Try making a new account and logging in with it
     */
    @Test
    public void check_login_US_06_01_02() {
        onView(withId(R.id.createAccButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText("Temp_User"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("p"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("CONFIRM"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        // login with new account
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText("Temp_User"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("password"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("LOGIN"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        // check if on the list page
        onView(withText("SEARCH")).check(matches(isDisplayed()));
        onView(withText("OPTIONS")).check(matches(isDisplayed()));
    }
    @After
    public void tearDown() {
        // there is no easy way to delete accounts right now.
    }
}
