package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.util.Checks.checkNotNull;



import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.sigma_blue.activities.LoginPageActivity;
import com.example.sigma_blue.activities.ViewListActivity;
import com.example.sigma_blue.entity.item.ItemList;

import com.google.firebase.firestore.auth.User;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewListActivityUITest extends UITestTools {
    @Rule
    public ActivityScenarioRule<LoginPageActivity> scenario = new
            ActivityScenarioRule<LoginPageActivity>(LoginPageActivity.class);

    /**
     * Login first for setting up the following user test
     * the first step of the all ViewList tests
     */
    public void testUser_Login() {
        UITestTools.login("Temp_User1", "password");
    }

    public void testUser_Signup() {
        UITestTools.signup("Temp_User1", "password");
    }


    /**
     * delete user account, function should start from ViewListActivity
     */
    public void User_Delete() {
        onView(withId(R.id.optionButton)).perform(click());
        onView(withText("Delete Account")).perform(click());
        onView(withText("CONFIRM")).perform(click());
    }

    /**
     * First, sign up a test user
     */
    @Before
    public void signUpUser() {
        testUser_Signup();
    }

    public void add_item_for_test() {

        // get to edit page
        onView(withId(R.id.addButton)).perform(click());

        // enter item info
        UITestTools.typeIn(R.id.text_name_disp, "iName");
        UITestTools.replaceWith(R.id.text_value_disp, "100");
        UITestTools.typeIn(R.id.text_make_disp, "Banana");
        UITestTools.typeIn(R.id.text_model_disp, "name");
        UITestTools.typeIn(R.id.text_serial_disp, "9001");
        UITestTools.typeIn(R.id.text_comment_disp, "comment about thing");
        UITestTools.typeIn(R.id.text_description_disp, "description of thing");

        // back to list
        onView(withId(R.id.button_save)).perform(click());

    }

    public void deleteItem (String itemName) {
        // delete
        onView(withText(itemName)).perform(click());
        onView(withId(R.id.button_delete)).perform(click());
        onView(withText("CONFIRM")).perform(click());
    }

    /**
     * As an owner, I want to add an item to my items, with a date of purchase or acquisition, brief
     * description, make, model, serial number (if applicable), estimated value, and comment.
     */
    @Test
    public void add_item_US_01_01_01() {

        // get the user login
        testUser_Login();

        // enter item info
        add_item_for_test();

        // check if the item is displayed properly
        onView(withText("iName")).check(matches(isDisplayed()));

        // clear itemList for next test
        deleteItem("iName");
    }


    /**
     * As an owner, I want to view an item and its details.
     */

    @Test
    public void view_item_US_01_02_01() {

        // get the test user login
        testUser_Login();
        //go to view page (items will persist between tests since everything is done on via the database

        add_item_for_test();

        // check if the item is displayed properly
        onView(withText("iName")).check(matches(isDisplayed()));

        onView(withText("iName")).perform(click());

        onView(withText("iName")).check(matches(isDisplayed()));
        onView(withText("100.0")).check(matches(isDisplayed()));
        onView(withText("Banana")).check(matches(isDisplayed()));
        onView(withText("name")).check(matches(isDisplayed()));
        onView(withText("9001")).check(matches(isDisplayed()));
        onView(withText("comment about thing")).check(matches(isDisplayed()));
        onView(withText("description of thing")).check(matches(isDisplayed()));

        onView(withId(R.id.button_back)).perform(click());

        // clear itemList for next test
        deleteItem("iName");
    }

    /**
     * As an owner, I want to edit the details of an item.
     */

    @Test
    public void edit_item_US_01_03_01() {

        // get the test user login
        testUser_Login();

        add_item_for_test();

        // modify name
        onView(withText("iName")).perform(click());
        onView(withId(R.id.button_edit)).perform(click());
        UITestTools.replaceWith(R.id.text_name_disp, "iName2");
        onView(withId(R.id.button_save)).perform(click());

        onView(withText("iName2")).check(matches(isDisplayed()));
        onView(withText("100.0")).check(matches(isDisplayed()));
        onView(withText("Banana")).check(matches(isDisplayed()));
        onView(withText("name")).check(matches(isDisplayed()));
        onView(withText("9001")).check(matches(isDisplayed()));
        onView(withText("comment about thing")).check(matches(isDisplayed()));
        onView(withText("description of thing")).check(matches(isDisplayed()));

        onView(withId(R.id.button_back)).perform(click());

        // clear itemList for next test
        deleteItem("iName2");

    }


    /**
     * As an owner, I want to delete an item.
     */
    @Test
    public void del_item_US_01_04_01() {

        // get the test user login
        testUser_Login();

        add_item_for_test();

        // delete
        deleteItem("iName");
        // check if item is deleted
        onView(withText("iName")).check(doesNotExist());
    }
//
//    /**
//     * As an owner, I want to see a list of my items.
//     */
//    @Test
//    public void list_items_US_02_01_01() {
//        // get to edit page
//        onView(withId(R.id.addButton)).perform(click());
//        // enter item info
//        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("iName"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("100"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        // back to list
//        onView(withId(R.id.button_save)).perform(click());
//        onView(withId(R.id.button_back)).perform(click());
//        // check if the item is displayed properly
//        onView(withId(R.id.listView))
//                .check(matches(atPosition(0, hasDescendant(withText("iName")))));
//
//        // get to edit page
//        onView(withId(R.id.addButton)).perform(click());
//        // enter item info
//        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("BetterName"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("150"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about better thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of better thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        // back to list
//        onView(withId(R.id.button_save)).perform(click());
//        onView(withId(R.id.button_back)).perform(click());
//        // check if the item is displayed properly
//        onView(withId(R.id.listView))
//                .check(matches(atPosition(0, hasDescendant(withText("BetterName")))));
//    }
//    /**
//     * As an owner, I want to see the total estimated value of the shown items in the list of items.
//     */
//    @Test
//    public void value_items_US_02_02_01() {
//        // get to edit page
//        onView(withId(R.id.addButton)).perform(click());
//        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("BetterName"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("150"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about better thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of better thing"));
//        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
//
//        onView(withText("$250.00")).check(matches(isDisplayed()));
//    }
//
////    /**
////     * As an owner, I want to select items from the list of items and delete the selected items.
////     */
////    @Test
////    public void delete_multiple_items_US_02_03_01() {
////    }
//
////    /**
////     * As an owner, I want to sort the list of items by date, description, make, or estimated value by ascending or descending order.
////     */
////    @Test
////    public void delete_multiple_items_US_02_04_01() {
////    }
//
////    /**
////     * As an owner, I want to filter the list of items by date range.
////     */
////    @Test
////    public void delete_multiple_items_US_02_05_01() {
////    }
//
////    /**
////     * As an owner, I want to filter the list of items by description keywords.
////     */
////    @Test
////    public void delete_multiple_items_US_02_06_01() {
////    }
//
////    /**
////     * As an owner, I want to filter the list of items by make.
////     */
////    @Test
////    public void delete_multiple_items_US_02_07_01() {
////    }
//
//    // Tag related testing done in the AddEditActivityUITest file
    @After
    public void tearDown() {
        // delete the test account
        User_Delete();

    }
}
