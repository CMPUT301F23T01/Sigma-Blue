package com.example.sigma_blue;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class DebuggingActivity extends BaseActivity implements ItemDB.ItemDBInteraction {


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

        /* Code section for linking UI elements */
        this.viewHolder = this.new ViewHolder();
        RecyclerView rvItemListView = findViewById(R.id.listView);

        /* Linking the adapter to the UI */
        rvItemListView.setAdapter(itemListAdapter);
        rvItemListView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        /* Setting up the on click listeners*/
        setUIOnClickListeners();


        // ITEM DATA BASE RELATED STUFF
        dbInit = DatabaseInitializer.newInstance();

        iDB = new ItemDB();

        iDB.signUp("testUser", "112233");
    }

    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> {
            this.itemListAdapter.addItem(
                new Item(
                        "ThinkPad", new Date(), "Nice UNIX book", "IBM",
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

            // FOR ItemDB TESTING PURPOSE

            login(iDB, "testUser", "112233", this);
            // after login, the

            // build a mock item


        });
    }

    @Override
    public void login(ItemDB idb, String userName, String password, Context Activity) {
        iDB.getDb().collection("SigmaBlue")
                .document(userName)
                .collection("AccountInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.getId().compareTo("Password") == 0) {
                                //check for the password
                                if (doc.getString("Password").compareTo(password) == 0) {
                                    iDB.setLoginUser(userName);
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast.makeText(Activity, "Successful Login", duration).show();
                                } else {
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast.makeText(Activity, "Failed to Login", duration).show();
                                }

                            }
                        }

                        // TEST Save To DB after login; FOR TESTING ONLY
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                        Date date = new Date();
                        try {
                            date = formatter.parse("2022-01");
                        } catch (ParseException e) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                        }
                        Item item = Item.newInstance("3090");

                        ArrayList<Item> testItemList = new ArrayList<Item>();
                        testItemList.add(item);
                        testItemList.add(new Item(
                                "ThinkPad", new Date(), "Nice UNIX book", "IBM",
                                "T460", 300f
                        ));
                        iDB.saveToDB(testItemList);
                    }
                });
    }

    // NOT YET Implemented
    @Override
    public ArrayList<Item> refreshFromDB(ItemDB idb) {
        return null;
    }

}
