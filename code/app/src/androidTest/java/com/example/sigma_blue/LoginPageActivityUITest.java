package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.sigma_blue.activities.LoginPageActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginPageActivityUITest {
    @Rule
    public ActivityScenarioRule<LoginPageActivity> scenario = new
            ActivityScenarioRule<LoginPageActivity>(LoginPageActivity.class);

    private ActivityScenario activityScenario;
    private View decorView;
    private UiDevice uiDevice;

    @Before
    public void setDecorView() {
        scenario.getScenario().onActivity( activity -> decorView = activity
                .getWindow().getDecorView());
    }


    @After
    public void tearDown() throws Exception {
        if (activityScenario != null) {
            activityScenario.close();
            activityScenario = null;
        } else ;
    }

    /**
     * As a user, I want a profile with a unique username.
     * Check if a invalid account gets denied
     */
    @Test
    public void check_login_US_06_01_01() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry
                .getInstrumentation());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText("Temp_user"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("password"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("LOGIN"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        // shouldn't be able to login with a bad account
    }
    /**
     * As a user, I want a profile with a unique username.
     * Try making a new account and logging in with it. Should be able to access
     * the list view.
     */
    @Test
    public void check_login_US_06_01_02() {
        onView(withId(R.id.createAccButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions
                .typeText("Temp_User1"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(ViewActions
                .typeText("password"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withText("CONFIRM"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        // login with new account
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(ViewActions.typeText("Temp_User1"));
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

}
