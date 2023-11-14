package com.example.sigma_blue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.VerifyException;

import java.util.Locale;
import java.util.Optional;

public class ViewListActivity extends BaseActivity {

    /* Tracking views that gets reused. Using nested class because struct */
    // https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private class ViewHolder extends RecyclerView.ViewHolder {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public Button deleteSelectedButton;
        public Button addTagsSelectedButton;
        public LinearLayout selectedItemsMenu;
        public FloatingActionButton addEntryButton;
        public TextView summaryView;

        /**
         * Construction of this nested class will bind the UI element to a 'package'
         */
        private ViewHolder(View itemView) {
            super(itemView);
            this.searchButton = findViewById(R.id.searchButton);
            this.sortFilterButton = findViewById(R.id.sortButton);
            this.optionsButton = findViewById(R.id.optionButton);
            this.addEntryButton = findViewById(R.id.addButton);
            this.summaryView = findViewById(R.id.summaryTextView);
            this.deleteSelectedButton = findViewById(R.id.deleteSelectedButton);
            this.addTagsSelectedButton = findViewById(R.id.addTagsSelectedButton);
            this.selectedItemsMenu = findViewById(R.id.selectedItemsMenu);
            /* Setting up defaults for the UI elements */
            setSummaryView(Optional.empty());
        }

        /**
         * This method updates the summary text view with the sum value. This
         * value can be empty.
         * @param sum is an Optional wrapper which will either contain the sum,
         *            or nothing.
         */
        public void setSummaryView(Optional<Float> sum) {
            if (sum.isPresent())this.summaryView
                    .setText(formatSummary(sum.get()));
            else this.summaryView
                    .setText(R.string.empty_summary_view);
        }

        /**
         * Returns the formatted output for the summary text view.
         * @param sum is a float that represents the sum
         * @return the formatted string.
         */
        public String formatSummary(Float sum) {
            return String.format(Locale.ENGLISH,
                    "The total value: %7.2f", sum);
        }
    }
    private final ActivityLauncher<Intent, ActivityResult> activityLauncher = ActivityLauncher.registerActivityForResult(this);
    private ViewHolder viewHolder;              // Encapsulation of the Views
    private GlobalContext globalContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        globalContext = GlobalContext.getInstance();

        if (globalContext.getAccount() == null) {
            //throw new VerifyException("Must have an account");
            return;
        }

        /* Code section for linking UI elements */
        RecyclerView rvItemListView = findViewById(R.id.listView);
        this.viewHolder = this.new ViewHolder(rvItemListView);

        /* ItemList encapsulates both the database and the adapter */
        globalContext.setUpItemList(this::handleClick, this::handleLongClick);
        globalContext.getItemList().setSummaryView(viewHolder.summaryView);

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(globalContext.getItemList().getAdapter());
        rvItemListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        // set up thing for selected items
        this.viewHolder.selectedItemsMenu.setVisibility(View.GONE);
        /* Setting up the on click listeners*/
        setUIOnClickListeners();
    }

    /**
     * listener used to deal with the user clicking on a thing in the list. This method is passed to
     * the ItemList constructor as a callback function, but not called directly from anywhere.
     * see <a href="https://stackoverflow.com/questions/24471109/recyclerview-onclick">...</a>,
     * answer from Marurban
     * @param item Clicked on item
     */
    private void handleClick(Item item) {
        Log.i("DEBUG", item.getName() + "Short Press");
        Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
        globalContext.setCurrentItem(item);
        globalContext.newState("details_fragment");
        startActivity(intent);
    }

    /**
     * listened to deal with long presses
     * @param item Item that was long pressed on
     */
    private void handleLongClick(Item item) {
        Log.i("DEBUG", item.getName() + "Long Press");
        globalContext.highlightItem(item);

        if (globalContext.getItemList().getAdapter().getHighlightedItems().size() > 0) {
            viewHolder.selectedItemsMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
        }
    }

    /**
     * Delete the selected items. Fully deletes them with no confirm
     */
    private void deleteSelectedItems() {
        for (Item i : globalContext.getHighlightedItems()) {
            globalContext.getItemList().remove(i);
        }
        globalContext.resetHighlightedItems();
        viewHolder.selectedItemsMenu.setVisibility(View.GONE);
    }

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            globalContext.setCurrentItem(null);
            globalContext.newState("add_item_fragment");
            startActivity(intent);
        });  // Launch add activity.
        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v -> {});
        viewHolder.optionsButton.setOnClickListener(v -> {});
        viewHolder.deleteSelectedButton.setOnClickListener(v -> {this.deleteSelectedItems();});

        viewHolder.addTagsSelectedButton.setOnClickListener(v -> {
            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
            globalContext.resetHighlightedItems();
            globalContext.setCurrentItem(null);
            globalContext.newState("multi_select_tag_manager_fragment");
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

    }
}