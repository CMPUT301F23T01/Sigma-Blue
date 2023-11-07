package com.example.sigma_blue.placeholder;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigma_blue.Account;
import com.example.sigma_blue.BaseActivity;
import com.example.sigma_blue.DatabaseInitializer;
import com.example.sigma_blue.FragmentLauncher;
import com.example.sigma_blue.Item;
import com.example.sigma_blue.ItemDB;
import com.example.sigma_blue.ItemList;
import com.example.sigma_blue.ItemListAdapter;
import com.example.sigma_blue.R;
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

public class DebuggingActivity extends BaseActivity {


    /* Tracking views that gets reused. Using nested class because struct */
    private class ViewHolder {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public FloatingActionButton addEntryButton;
        public TextView summaryView;

        /**
         * Construction of this nested class will bind the UI element to a 'package'
         */
        public ViewHolder() {
            this.searchButton = findViewById(R.id.searchButton);
            this.sortFilterButton = findViewById(R.id.sortButton);
            this.optionsButton = findViewById(R.id.optionButton);
            this.addEntryButton = findViewById(R.id.addButton);
            this.summaryView = findViewById(R.id.summaryTextView);

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

        public void setViewText(final TextView v, final String s) {
            v.setText(s);
        }
    }

    public ItemListAdapter itemListAdapter;     // The itemListAdapter
    private FragmentLauncher fragmentLauncher;
    private ViewHolder viewHolder;              // Encapsulation of the Views
    private DatabaseInitializer dbInit;

    private ItemDB iDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        /* Setting up the data. TODO: Make this use the database */
        itemListAdapter = ItemListAdapter.newInstance(ItemList.newInstance());
        fragmentLauncher = FragmentLauncher.newInstance(this);  // Embedding the fragment
        iDB = ItemDB.newInstance(new Account("Watrina 2",
                "dsiaflk1j"));
        iDB.startListening(itemListAdapter, itemListAdapter.getItemList());

        /* Code section for linking UI elements */
        this.viewHolder = this.new ViewHolder();
        RecyclerView rvItemListView = findViewById(R.id.listView);

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(itemListAdapter);
        rvItemListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        /* Setting up the on click listeners*/
        setUIOnClickListeners();

        /* Debugging activity specifics */
        viewHolder.setViewText(viewHolder.sortFilterButton, "Button 1");

        // ITEM DATA BASE RELATED STUFF
        dbInit = DatabaseInitializer.newInstance();

    }

    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> {
//            this.itemListAdapter.addItem(
//                new Item(
//                        "ThinkPad", new Date(),
//                        "Nice UNIX book", "", "IBM",
//                        "T460", 300f
//                )
//            );

            this.iDB.addItem(
                    new Item(
                            "ThinkPad", new Date(),
                            "Nice UNIX book", "", "IBM",
                            "T460", 300f
                    )
            );

            /* Updates the summation */
            this.viewHolder.setSummaryView(itemListAdapter.sumValues());
        });  // Launch add fragment.
        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v -> {
             if (dbInit.checkExistence(new Account("Watrina",
                    "lkj312"))) Toast.makeText(this,
                     "Exists", Toast.LENGTH_SHORT).show();
             else Toast.makeText(this, "Does not exist",
                     Toast.LENGTH_SHORT).show();
        });
        viewHolder.optionsButton.setOnClickListener(v -> {



        });
    }
}
