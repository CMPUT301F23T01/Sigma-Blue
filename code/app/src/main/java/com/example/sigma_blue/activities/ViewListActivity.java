package com.example.sigma_blue.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;

import com.example.sigma_blue.entity.item.ItemListAdapter;

import com.example.sigma_blue.fragments.QueryFragment;
import com.example.sigma_blue.query.QueryGenerator;
import com.example.sigma_blue.query.SortField;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;


public class ViewListActivity extends BaseActivity {

    /* Tracking views that gets reused. Using nested class because struct */
    // https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private class ViewHolder {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public Button deleteSelectedButton;
        public Button addTagsSelectedButton;
        public ConstraintLayout selectedItemsMenu;
        public FloatingActionButton addEntryButton;
        public TextView summaryView;
        public ListView listListView;

        /**
         * Construction of this nested class will bind the UI element to a 'package'
         */
        private ViewHolder() {
            this.listListView = findViewById(R.id.listView);
            this.searchButton = findViewById(R.id.searchButton);
            this.sortFilterButton = findViewById(R.id.sortButton);
            this.optionsButton = findViewById(R.id.optionButton);
            this.addEntryButton = findViewById(R.id.addButton);
            this.summaryView = findViewById(R.id.summaryTextView);
            this.deleteSelectedButton = findViewById(R.id.deleteSelectedButton);
            this.addTagsSelectedButton = findViewById(R.id.addTagsSelectedButton);
            this.selectedItemsMenu = findViewById(R.id.selectedItemsMenu);
        }
    }

    // Used for launching new activities. Potentially unused right now
    private final ActivityLauncher<Intent, ActivityResult> activityLauncher
            = ActivityLauncher.registerActivityForResult(this);
    private ViewHolder viewHolder;              // Encapsulation of the Views

    private GlobalContext globalContext;        // Global context object
    private FragmentManager fragmentManager;    // For getting queries

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState); // Activity super
        setContentView(R.layout.view_list); // sets up the activity layout

        globalContext = GlobalContext.getInstance();

        if (globalContext.getAccount() == null) {
            // sometimes another thread ends up here with an empty global context
            // TODO: figure out why/how that happens.
            //throw new VerifyException("Must have an account");
            return;
        }

        /* Code section for linking UI elements */
        ListView itemListView = findViewById(R.id.listView);
        this.viewHolder = this.new ViewHolder();

        /* ItemList encapsulates both the database and the adapter */
        globalContext.getItemList().setAdapter(
                new ItemListAdapter(globalContext.getItemList().getList(), this, viewHolder.summaryView));
        globalContext.getItemList().startListening();

        globalContext.getItemList().setSummaryView(viewHolder.summaryView);

        /* Linking the adapter to the UI */
        itemListView.setAdapter(globalContext.getItemList().getAdapter());

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
        Item currentItem = new Item(item);
        globalContext.setCurrentItem(currentItem);
        globalContext.setModifiedItem(item);
        globalContext.newState(ApplicationState.DETAILS_FRAGMENT);
        startActivity(intent);
    }


    /**
     * Delete the selected items. Fully deletes them with no confirm
     * TODO: If have time, add a confirm button
     */
    private void deleteSelectedItems() {
        for (Item i : globalContext.getSelectedItems().getSelected()) {
            globalContext.getItemList().remove(i);
        }
        globalContext.getSelectedItems().resetSelected();
        globalContext.getItemList().getAdapter().notifyDataSetChanged();
        viewHolder.selectedItemsMenu.setVisibility(View.GONE);
    }

    /**
     * This method shows the query fragment for the user to choose either a sort
     * or a filter, or maybe both.
     */
    private void displayQueryFragment() {
        QueryFragment queryFragment = new QueryFragment();
        globalContext.newState(ApplicationState.SORT_MENU);
        startFragmentTransaction(queryFragment, ApplicationState.SORT_MENU
                .toString());
    }

    /**
     * Launches DialogFragment.
     * @param fragment the dialog fragment class being launched
     * @param tag the tag of the fragment
     */
    private void startFragmentTransaction(DialogFragment fragment, String tag) {
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        fragment.show(fragmentManager, tag);
    }

    /**
     * This method sets all the on click listeners for all the interactive UI elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            globalContext.setCurrentItem(null);
            globalContext.setModifiedItem(new Item());
            globalContext.newState(ApplicationState.ADD_ITEM_FRAGMENT);
            startActivity(intent);
        });  // Launch add activity.

        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v ->
                this.displayQueryFragment());

        viewHolder.optionsButton.setOnClickListener(v -> {});

        viewHolder.deleteSelectedButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage("Please confirm the deletion of the selected item(s).");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSelectedItems();
                            }
                });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        });

        viewHolder.addTagsSelectedButton.setOnClickListener(v -> {
            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
            globalContext.setCurrentItem(null);
            globalContext.newState(ApplicationState
                    .MULTI_SELECT_TAG_MANAGER_FRAGMENT);
            Log.i("NEW STATE", ApplicationState
                    .MULTI_SELECT_TAG_MANAGER_FRAGMENT.toString());
            Intent intent = new Intent(ViewListActivity.this,
                    AddEditActivity.class);
            startActivity(intent);
        });

        viewHolder.listListView // This is for short clicks on a row
                .setOnItemClickListener((parent, view, position, id) -> {
                    this.handleClick(globalContext.getItemList()
                            .getList().get(position));
        });

        /* The long click listener */
        viewHolder.listListView.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    final Item itemCache = globalContext.getItemList()
                            .getList().get(position);
                    this.handleLongClick(itemCache);

                    globalContext.getItemList().getAdapter()
                            .notifyDataSetChanged();    // Update highlight

                    /*Returns true if the list consumes the click. Always true
                    * in our app*/
                    return true;
                });
    }

    /**
     * listened to deal with long presses
     * @param item Item that was long pressed on
     */
    private void handleLongClick(Item item) {
//        Log.i("DEBUG", item.getName() + " Long Press");
        globalContext.getSelectedItems().toggleHighlight(item);

        if (!globalContext.getSelectedItems().empty()) {
            viewHolder.selectedItemsMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
        }
    }

}
