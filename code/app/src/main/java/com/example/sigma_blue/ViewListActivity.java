package com.example.sigma_blue;

import android.os.Bundle;
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
        public FloatingActionButton addEntryButton;

    }

    /* The ItemListAdapter */
    public ItemListAdapter itemListAdapter;
    private FragmentLauncher fragmentLauncher;

    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        this.viewHolder = this.new ViewHolder();
        setContentView(R.layout.view_list);

        /* Setting up the data. TODO: Make this use the database */
        itemListAdapter = ItemListAdapter.newInstance(ItemList.newInstance());
        fragmentLauncher = FragmentLauncher.newInstance(this);  // Embedding the fragment

        /* Code section for linking UI elements */
        viewHolder.addEntryButton = findViewById(R.id.addButton);
        viewHolder.searchButton = findViewById(R.id.searchButton);
        RecyclerView rvItemListView = findViewById(R.id.listView);

        /* Adding to the adapter for testing */
        itemListAdapter.addItem(new Item(
                "3080",
                new Date(),
                "Description",
                "EVGA",
                "RTX 3080",
                38.0f
        ));

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(itemListAdapter);
        rvItemListView.setLayoutManager(new LinearLayoutManager(this));
    }

    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};
}
