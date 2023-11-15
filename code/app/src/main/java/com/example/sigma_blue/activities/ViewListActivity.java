package com.example.sigma_blue.activities;

import static java.util.Objects.isNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.ColorInt;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.item.ItemDB;
import com.example.sigma_blue.entity.item.ItemList;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.item.ItemListAdapter;
import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.fragments.FragmentLauncher;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class ViewListActivity extends BaseActivity {

    /* Tracking views that gets reused. Using nested class because struct */
    // https://stackoverflow.com/questions/24471109/recyclerview-onclick
    private class ViewHolder {
        public Button searchButton;
        public Button sortFilterButton;
        public Button optionsButton;
        public Button deleteSelectedButton;
        public Button addTagsSelectedButton;
        public LinearLayout selectedItemsMenu;
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

    private FragmentLauncher fragmentLauncher;
    private final ActivityLauncher<Intent, ActivityResult> activityLauncher
            = ActivityLauncher.registerActivityForResult(this);
    private ViewHolder viewHolder;              // Encapsulation of the Views
    private ItemList itemList;
    private Account currentAccount;
    private List<Integer> selectedIndex;

    private ArrayList<Item> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Setting up the basics of the activity */
        super.onCreate(savedInstanceState); // Activity super
        setContentView(R.layout.view_list); // sets up the activity layout
        selectedIndex = new ArrayList<>();

        /* Obtaining the passed in data */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentAccount = (Account) extras.getSerializable("account");
        } else {
            Log.e("DEBUG", "No Account object in bundle!");
            currentAccount = new Account("UI_Test_User", "password");
        }

        /* Code section for linking UI elements */
        this.viewHolder = this.new ViewHolder();

        /* ItemList encapsulates both the database and the adapter */
        this.itemList = ItemList.newInstance(currentAccount,
                ItemDB.newInstance(currentAccount),
                new ItemListAdapter(this, viewHolder.summaryView));
        itemList.setSummaryView(viewHolder.summaryView);
        fragmentLauncher = FragmentLauncher.newInstance(this);  // Embedding the fragment

        /* Linking the adapter to the UI */
        viewHolder.listListView.setAdapter(itemList.getListAdapter());

        // set up thing for selected items
        this.viewHolder.selectedItemsMenu.setVisibility(View.GONE);
        this.selectedItems = new ArrayList<Item>();
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
        Log.i("DEBUG", item.getName() + "Sort Press");
        Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("account", currentAccount);
        intent.putExtra("mode", "edit");
        activityLauncher.launch(intent, this::processNewItemResult);
    }

    /**
     * listened to deal with long presses
     * @param item Item that was long pressed on
     */
    private void handleLongClick(Item item) {
        Log.i("DEBUG", item.getName() + "Long Press");

//        if (itemList.getRecyclerAdapter().getHighlightedItems().size() > 0) {
//            viewHolder.selectedItemsMenu.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
//        }
    }

    /**
     * Either adds a new item to the list or updates an existing one.
     * @param result result from the AddEditActivity
     */
    protected void processNewItemResult(ActivityResult result) {
        Bundle extras = result.getData().getExtras();
        //Item testItem = new Item("ThinkPad", new Date(), "Nice UNIX book","", "IBM", "T460", 300f);
        Item updatedItem = null;
        String updatedItemID = null;
        boolean onDeletion = false;
        try {
            updatedItem = (Item) extras.getSerializable("item");
            updatedItemID = extras.getString("id");
            onDeletion = extras.getBoolean("onDeletion");
        } catch (NullPointerException e) {
            Log.e("DEBUG", "New intent without extras! (canceling add an item)");
        }

        if (updatedItem == null) {
            Log.e("DEBUG", "Null updated item");
            return;
        }

        if (onDeletion) {
            this.itemList.remove(updatedItem);
        } else if (Objects.equals(updatedItemID, "") || updatedItemID == null) {
            this.itemList.add(updatedItem);
        } else {
            this.itemList.updateItem(updatedItem, updatedItemID);
        }

    }

    /**
     * Delete the selected items. Fully deletes them with no confirm
     */
    private void deleteSelectedItems() {
        for (Item i : this.selectedItems) {
            itemList.remove(i);
        }
        selectedItems.clear();
        viewHolder.selectedItemsMenu.setVisibility(View.GONE);
    }

    /**
     * Updates the tags of highlighted items, given the list of user selected tags
     * @param result result sent back from activity
     */
    private void applyTagResults(ActivityResult result) {
        Bundle extras = result.getData().getExtras();
        ArrayList<Tag> tags = null;
        try {
            tags = ((Item) extras.getSerializable("item")).getTags();
        } catch (NullPointerException e) {
            Log.e("DEBUG", "Apply tags, but no tags returned");
        }

//        if (!isNull(tags)) {
//            for (Tag t : tags) {
//                for (Item i : itemList.getRecyclerAdapter().getHighlightedItems()) {
//                    i.addTag(t);
//                }
//            }
//        }
    }
    /* Fragment result listeners are lambda expressions that controls what the class does when the
    * results are received.*/
    FragmentResultListener addFragmentResultListener = (requestKey, result) -> {};

    /**
     * This method sets all the on click listeners for all the interactive UI
     * elements.
     */
    private void setUIOnClickListeners() {
        viewHolder.addEntryButton.setOnClickListener(v -> {
            Item newItem = new Item();
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            intent.putExtra("item", newItem);
            intent.putExtra("mode", "add");
            activityLauncher.launch(intent, this::processNewItemResult);

        });  // Launch add activity.
        viewHolder.searchButton.setOnClickListener(v -> {});    // Launch search fragment
        viewHolder.sortFilterButton.setOnClickListener(v -> {});
        viewHolder.optionsButton.setOnClickListener(v -> {});
        viewHolder.deleteSelectedButton.setOnClickListener(v -> {
            this.deleteSelectedItems();});
        /*
        viewHolder.addTagsSelectedButton.setOnClickListener(v -> {
            viewHolder.selectedItemsMenu.setVisibility(View.GONE);
            itemList.getAdapter().resetHighlightedItems();
            Intent intent = new Intent(ViewListActivity.this, AddEditActivity.class);
            intent.putExtra("mode", "multi_tag");
            intent.putExtra("account", currentAccount);
            activityLauncher.launch(intent, this::applyTagResults);
        });
         */

        /* The List View related control flow */
        viewHolder.listListView.setLongClickable(true); // Turning on long click

        viewHolder.listListView // This is for short clicks on a row
                .setOnItemClickListener((parent, view, position, id) -> {
                    Item item = itemList.getItem(position);
                    Log.i("DEBUG", item.getName() + "Sort Press");
                    Intent intent = new Intent(ViewListActivity
                            .this, AddEditActivity.class);
                    intent.putExtra("item", item);
                    intent.putExtra("account", currentAccount);
                    intent.putExtra("mode", "edit");
                    activityLauncher.launch(intent, this::processNewItemResult);
        });

        /* The long click listener */
        viewHolder.listListView.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    boolean contained = selectedIndex.contains(position);
                    highlightControl(view, !contained);
                    view.setSelected(!contained);
                    if (contained) selectedIndex.remove(Integer
                            .valueOf(position));
                    else selectedIndex.add(Integer.valueOf(position));

                    // Selected menu visibility when at least one selected
                    if (this.selectedIndex.size() > 0) {
                        viewHolder.selectedItemsMenu.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.selectedItemsMenu.setVisibility(View.GONE);
                    }

                    return true;
                });
    }

    /**
     * Method that will turn on the highlight of the view if it is selected,
     * otherwise reset it to the default background colour.
     * @param view is the view that is being checked.
     */
    private void highlightControl(View view, boolean selected) {
        @ColorInt int rowColor;
        if (selected) rowColor = MaterialColors
                .getColor(view, com.google.android.material.R.attr
                        .colorSecondary);
        else rowColor = MaterialColors
                .getColor(view, com.google.android.material.R.attr
                        .colorOnBackground);
        view.setBackgroundColor(rowColor);
    }

    /**
     * Part of testing frameworks. This method sends the item back.
     * @return ItemList is the object that controls the items.
     */
    public ItemList getItemList() {
        return this.itemList;
    }
}
