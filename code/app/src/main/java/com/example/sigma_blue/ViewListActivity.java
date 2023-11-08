package com.example.sigma_blue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class ViewListActivity extends BaseActivity {

    /* Tracking views that gets reused. Using nested class because struct */
    // https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public FloatingActionButton addEntryButton;
        public TextView summaryView;

        /**
         * Construction of this nested class will bind the UI element to a 'package'
         */
        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.searchButton = findViewById(R.id.searchButton);
            this.sortFilterButton = findViewById(R.id.sortButton);
            this.optionsButton = findViewById(R.id.optionButton);
            this.addEntryButton = findViewById(R.id.addButton);
            this.summaryView = findViewById(R.id.summaryTextView);

            /* Setting up defaults for the UI elements */
            setSummaryView(Optional.empty());
        }
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
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

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public ItemListAdapter itemListAdapter;
    private FragmentLauncher fragmentLauncher;
    private ViewHolder viewHolder;              // Encapsulation of the Views
    private ItemList itemList;
    private final Account placeHolderAccount = new Account("Watrina 3",
            "flsdkjqi1121-");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
        this.viewHolder = this.new ViewHolder();

        /* ItemList encapsulates both the database and the adapter */
        this.itemList = ItemList.newInstance(placeHolderAccount);
        itemList.setSummaryView(viewHolder.summaryView);
        fragmentLauncher = FragmentLauncher.newInstance(this);  // Embedding the fragment

        /* Code section for linking UI elements */
        RecyclerView rvItemListView = findViewById(R.id.listView);
        this.viewHolder = this.new ViewHolder(rvItemListView);

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(itemList.getAdapter());
        rvItemListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        /* Setting up the on click listeners*/
        setUIOnClickListeners();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        Item testItem = new Item();
        Item updatedItem = null;
        Integer updatedItemID = null;
        try {
            updatedItem = (Item) extras.getSerializable("item");
            updatedItemID = extras.getInt("id");
        } catch (NullPointerException e) {
            Log.e("DEBUG", "New intent without extras!");
        }

        itemListAdapter.addItem(testItem);
        if (updatedItem == null) {
            Log.e("DEBUG", "Null updated item");
            return;
        }

        if (updatedItemID == null) {
            itemListAdapter.addItem(updatedItem);
        } else {
            // problem for later
        }

    }

    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
//        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri uri) {
//                        uri.toString();
//                    }
//                });
//
//        viewHolder.addEntryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Pass in the mime type you want to let the user select
//                // as the input
//                mGetContent.launch("image/*");
//            }
//        });

        viewHolder.addEntryButton.setOnClickListener(v -> {
            Item newItem = new Item();
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            intent.putExtra("item", newItem);
            startActivity(intent);
            // this.itemList.add(
            //     new Item(
            //             "ThinkPad", new Date(), "Nice UNIX book","", "IBM",
            //             "T460", 300f
            //     )
            // );

        });  // Launch add fragment.
        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v -> {});
        viewHolder.optionsButton.setOnClickListener(v -> {});
        RecyclerView rvItemListView = (RecyclerView) findViewById(R.id.listView);
    }
}
