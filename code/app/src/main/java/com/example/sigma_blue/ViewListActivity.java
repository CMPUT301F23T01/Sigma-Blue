package com.example.sigma_blue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class ViewListActivity extends BaseActivity{
    /* Tracking views that gets reused. Using nested class because struct */
    private class ViewHolder {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public FloatingActionButton addEntryButton;

        /**
         * Construction of this nested class will bind the UI element to a 'package'
         */
        public ViewHolder() {
            this.searchButton = findViewById(R.id.searchButton);
            this.sortFilterButton = findViewById(R.id.sortButton);
            this.optionsButton = findViewById(R.id.optionButton);
            this.addEntryButton = findViewById(R.id.addButton);

        }
    }

    public ItemListAdapter itemListAdapter;     // The itemListAdapter
    private FragmentLauncher fragmentLauncher;
    private ViewHolder viewHolder;              // Encapsulation of the Views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        /* Setting up the data. TODO: Make this use the database */
        itemListAdapter = ItemListAdapter.newInstance(ItemList.newInstance());
        fragmentLauncher = FragmentLauncher.newInstance(this);  // Embedding the fragment

        /* Code section for linking UI elements */
        this.viewHolder = this.new ViewHolder();
        RecyclerView rvItemListView = findViewById(R.id.listView);

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(itemListAdapter);
        rvItemListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        /* Adding to the adapter for testing */
        itemListAdapter.addItem(new Item(
                "3080",
                new Date(),
                "Description",
                "Comment",
                "EVGA",
                "RTX 3080",
                38.0f
        ));

        /* Setting up the on click listeners*/
        setUIOnClickListeners();
    }

    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> this.itemListAdapter.addItem(
                new Item(
                        "ThinkPad", new Date(), "Nice UNIX book","","IBM",
                        "T460", 300f
                )
        ));  // Launch add fragment.
        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v -> {});
        viewHolder.optionsButton.setOnClickListener(v -> {});
    }

}
