package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
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
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
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

    @Rule
    public final ActivityTestRule<ViewListActivity> mActivityRule = new ActivityTestRule<>(ViewListActivity.class);

    //https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    //https://stackoverflow.com/questions/40140700/testing-recyclerview-if-it-has-data-with-espresso
    private int getRVcount(){
        RecyclerView recyclerView = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.listView);
        return recyclerView.getAdapter().getItemCount();
    }

    /**
     * As an owner, I want to add an item to my items, with a date of purchase or acquisition, brief
     * description, make, model, serial number (if applicable), estimated value, and comment.
     */
    @Test
    public void add_item_US_01_01_01() {
        // get to edit page
        onView(withId(R.id.addButton)).perform(click());
        //onView(withId(R.id.button_edit)).perform(click());
        // enter item info
        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("iName"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("100"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        // back to list
        onView(withId(R.id.button_save)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
        // check if the item is displayed properly
        onView(withId(R.id.listView))
                .check(matches(atPosition(0, hasDescendant(withText("iName")))));
    }

    /**
     * As an owner, I want to view an item and its details.
     */
    @Test
    public void view_item_US_01_02_01() {
        // go to view page (items will persist between tests since everything is done on via the database
//        onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("iName")).check(matches(isDisplayed()));
        onView(withText("100.00")).check(matches(isDisplayed()));
        onView(withText("Banana")).check(matches(isDisplayed()));
        onView(withText("name")).check(matches(isDisplayed()));
        onView(withText("9001")).check(matches(isDisplayed()));
        onView(withText("comment about thing")).check(matches(isDisplayed()));
        onView(withText("description of thing")).check(matches(isDisplayed()));
    }

    /**
     * As an owner, I want to edit the details of an item.
     */
    @Test
    public void edit_item_US_01_03_01() {
        // go to view page (items will persist between tests since everything is done on via the database
//        onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // modify name
        onView(withId(R.id.button_edit)).perform(click());
        onView(withId(R.id.text_name_disp)).perform(replaceText("iName2")).perform(closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());

        onView(withText("iName2")).check(matches(isDisplayed()));
        onView(withText("100.00")).check(matches(isDisplayed()));
        onView(withText("Banana")).check(matches(isDisplayed()));
        onView(withText("name")).check(matches(isDisplayed()));
        onView(withText("9001")).check(matches(isDisplayed()));
        onView(withText("comment about thing")).check(matches(isDisplayed()));
        onView(withText("description of thing")).check(matches(isDisplayed()));
    }

    /**
     * As an owner, I want to delete an item.
     */
    @Test
    public void del_item_US_01_04_01() {
        // go to view page (items will persist between tests since everything is done on via the database
//        onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // delete
        onView(withId(R.id.button_delete)).perform(click());
        assert(getRVcount() == 0);
    }

    /**
     * As an owner, I want to see a list of my items.
     */
    @Test
    public void list_items_US_02_01_01() {
        // get to edit page
        onView(withId(R.id.addButton)).perform(click());
        //onView(withId(R.id.button_edit)).perform(click());
        // enter item info
        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("iName"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("100"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        // back to list
        onView(withId(R.id.button_save)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
        // check if the item is displayed properly
        onView(withId(R.id.listView))
                .check(matches(atPosition(0, hasDescendant(withText("iName")))));
        // get to edit page
        onView(withId(R.id.addButton)).perform(click());
        //onView(withId(R.id.button_edit)).perform(click());
        // enter item info
        onView(withId(R.id.text_name_disp)).perform(ViewActions.typeText("BetterName"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("150"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about better thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of better thing"));
        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard());
        // back to list
        onView(withId(R.id.button_save)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
        // check if the item is displayed properly
        onView(withId(R.id.listView))
                .check(matches(atPosition(0, hasDescendant(withText("BetterName")))));
    }
    /**
     * As an owner, I want to see the total estimated value of the shown items in the list of items.
     */
    @Test
    public void value_items_US_02_02_01() {
        onView(withText("$250.00")).check(matches(isDisplayed()));
    }

//    /**
//     * As an owner, I want to select items from the list of items and delete the selected items.
//     */
//    @Test
//    public void delete_multiple_items_US_02_03_01() {
//    }

//    /**
//     * As an owner, I want to sort the list of items by date, description, make, or estimated value by ascending or descending order.
//     */
//    @Test
//    public void delete_multiple_items_US_02_04_01() {
//    }

//    /**
//     * As an owner, I want to filter the list of items by date range.
//     */
//    @Test
//    public void delete_multiple_items_US_02_05_01() {
//    }

//    /**
//     * As an owner, I want to filter the list of items by description keywords.
//     */
//    @Test
//    public void delete_multiple_items_US_02_06_01() {
//    }

//    /**
//     * As an owner, I want to filter the list of items by make.
//     */
//    @Test
//    public void delete_multiple_items_US_02_07_01() {
//    }

    // Tag related testing done in the AddEditActivityUITest file
    @After
    public void tearDown() {
        // delete any items made by running the tests
        if(getRVcount() > 0) {
            for (int i = 0; i < getRVcount(); i++) {

                // click on item
//                onView(withId(R.id.listView)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
                // delete item
                onView(withId(R.id.deleteButton)).perform(click());
            }
        }
    }
}
