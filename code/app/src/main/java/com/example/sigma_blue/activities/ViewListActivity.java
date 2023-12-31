package com.example.sigma_blue.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;

import com.example.sigma_blue.entity.item.ItemListAdapter;

import com.example.sigma_blue.fragments.QueryFragment;

import com.example.sigma_blue.utility.ConfirmDelete;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity that displays the list of items. Acts like the 'main page' of the app.
 */
public class ViewListActivity extends AppCompatActivity {

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
                new ItemListAdapter(globalContext.getItemList().getVisibleList(), this, viewHolder.summaryView));
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
        Item modifiedItem = new Item(item);
        globalContext.setCurrentItem(currentItem);
        globalContext.setModifiedItem(modifiedItem);
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
        globalContext.getItemList().updateUI();
        viewHolder.selectedItemsMenu.setVisibility(View.GONE);
    }

    /**
     * This method shows the query fragment for the user to choose either a sort
     * or a filter, or maybe both.
     */
    private void displayQueryFragment(ApplicationState state) {
        QueryFragment queryFragment = new QueryFragment();
        globalContext.newState(state);
        startFragmentTransaction(queryFragment, state.toString());
    }

    /**
     * Launches DialogFragment.
     * @param fragment the dialog fragment class being launched
     * @param tag the tag of the fragment
     */
    private void startFragmentTransaction(DialogFragment fragment, String tag) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
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

        viewHolder.searchButton.setOnClickListener(v ->
                this.displayQueryFragment(ApplicationState.FILTER_MENU));

        viewHolder.sortFilterButton.setOnClickListener(v ->
                this.displayQueryFragment(ApplicationState.SORT_MENU));

        viewHolder.optionsButton.setOnClickListener(v ->
                this.handleOptionsClick());

        viewHolder.deleteSelectedButton.setOnClickListener(v -> {
            // method for confirm delete menu, creates onClickListener for specific method of deleting
            ConfirmDelete.confirmDelete(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // code for deleting that is to be run if delete is confirmed by user
                    deleteSelectedItems();
                }
            });  
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
                            .getVisibleList().get(position));
        });

        /* The long click listener */
        viewHolder.listListView.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    final Item itemCache = globalContext.getItemList()
                            .getVisibleList().get(position);
                    this.handleLongClick(itemCache);

                    globalContext.getItemList().updateUI();    // Update highlight

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

    /**
     * Open the options menu.
     */
    private void handleOptionsClick() {
        // same pattern as add edit fragment add picture button. Do we want to move this to a fragment?
        final CharSequence[] optionsMenu = {"Logout", "Delete Account", "Cancel" };
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewListActivity.this);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Logout")){
                    logoutUser();
                }
                else if(optionsMenu[i].equals("Delete Account")){
                    ConfirmDelete.confirmDelete(ViewListActivity.this, (dialog, which) -> handleDeleteAccount());
                }
                else if (optionsMenu[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    /**
     * Delete the currently logged in account
     */
    private void handleDeleteAccount() {
        // delete account
        globalContext.getItemList().removeAll();
        globalContext.getTagList().removeAll();
        globalContext.getAccountList().remove(globalContext.getAccount());

        // go back to login page
        this.logoutUser();
    }

    /**
     * go back to the login page and log the user out.
     */
    private void logoutUser() {
        globalContext.setAccount(null);
        Intent intent = new Intent(ViewListActivity.this, LoginPageActivity.class);
        startActivity(intent);
    }
}
