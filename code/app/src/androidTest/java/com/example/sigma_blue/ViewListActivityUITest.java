package com.example.sigma_blue;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
import androidx.test.espresso.contrib.RecyclerViewActions;
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
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_value_disp)).perform(ViewActions.typeText("100"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_make_disp)).perform(ViewActions.typeText("Banana"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_model_disp)).perform(ViewActions.typeText("name"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_serial_disp)).perform(ViewActions.typeText("9001"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_comment_disp)).perform(ViewActions.typeText("comment about thing"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.text_description_disp)).perform(ViewActions.typeText("description of thing"));
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard());
        // back to list
        onView(withId(R.id.button_save)).perform(click());
        onView(withId(R.id.button_back)).perform(click());
        // check if the item is displayed properly
        onView(withId(R.id.listView))
                .check(matches(atPosition(0, hasDescendant(withText("iName")))));

    }
    @After
    public void tearDown() {
        // delete any items made by running the tests
        if(getRVcount() > 0) {
            for (int i = 0; i < getRVcount(); i++) {

                // click on item
                onView(withId(R.id.listView))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
                // delete item
                onView(withId(R.id.deleteButton)).perform(click());

            }
        }
    }
}
