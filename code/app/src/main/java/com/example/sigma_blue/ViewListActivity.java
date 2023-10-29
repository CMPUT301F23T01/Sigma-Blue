package com.example.sigma_blue;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewListActivity extends BaseActivity{
    /* Tracking views that gets reused */
    private Button searchButton;
    private FloatingActionButton addEntryButton;

    /* The ItemListAdapter */
    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        /* Setting up the data. TODO: Make this use the database */
        this.itemListAdapter = ItemListAdapter.newInstance(ItemList.newInstance(), this);

        /* Code section for linking UI elements */
        addEntryButton = findViewById(R.id.addButton);
        searchButton = findViewById(R.id.searchButton);
    }
}
