package com.example.sigma_blue.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.context.AddEditViewModel;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.item.item.Item;

import java.util.Objects;

/**
 * Add/Edit activity class. Controls the add and edit activity.
 */
public class AddEditActivity extends BaseActivity
{
    // TODO: Add these modes to a global enum file
    private static final String ARG_ACC = "account";
    private static final String ARG_ITEM = "item"; // item key accessor
    private static final String ARG_MODE = "mode"; // item mode accessor
    private static final String ARG_ID = "id"; // item id accessor
    private static final String ARG_DELETE_FLAG = "onDeletion"; // item deletion flag accessor
    private AddEditViewModel sharedVM; // Shared ViewModel
    private Account currentAccount; // Account of the user currently logged in
                                    // We will need pass this from ViewListActivity for DB interactions

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set xml view
        setContentView(R.layout.add_edit_activity);

        // Add bundle to shared ViewHolder
        Bundle bundledItem;
        if (savedInstanceState == null) { bundledItem = getIntent().getExtras(); }
        else { bundledItem = savedInstanceState; }
        Item currentItem = (Item) bundledItem.getSerializable(ARG_ITEM);
        currentAccount = (Account) bundledItem.getSerializable(ARG_ACC);

        String id = "";

        if (currentItem == null) {
            currentItem = new Item();
        } else {
            id = currentItem.getDocID();
        }
        String mode = bundledItem.getString(ARG_MODE);

        sharedVM = new ViewModelProvider(this).get(AddEditViewModel.class);
        sharedVM.setItem(currentItem);
        sharedVM.setMode(mode);
        sharedVM.setId(id);
        sharedVM.setDeleteFlag(false);

        // Setup nav controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        NavGraph graph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        if (Objects.equals(mode, "add"))
        {
            graph.setStartDestination(R.id.editFragment);
        }
        else if (Objects.equals(mode, "multi_tag"))
        {
            graph.setStartDestination(R.id.tagManagerFragment);
        }
        else if (Objects.equals(mode, "edit"))
        {
            graph.setStartDestination(R.id.detailsFragment);
        }
        else
        {
            Log.e("DEBUG", "Bad AddEditMode");
        }
        navController.setGraph(graph, bundledItem);
    }

    /**
     * return item back to ViewListActivity and close activity
     */
    public void returnAndClose()
    {
        Intent i = new Intent(this, ViewListActivity.class);

        // Add shared item and flags to intent
        i.putExtra(ARG_ITEM, sharedVM.getItem().getValue());
        Log.e("DEBUG", sharedVM.getItem().getValue().toString());
        i.putExtra(ARG_MODE, sharedVM.getMode().getValue());
        Log.e("DEBUG", sharedVM.getMode().getValue().toString());
        i.putExtra(ARG_ID, sharedVM.getId().getValue());
        Log.e("DEBUG", sharedVM.getId().getValue().toString());
        i.putExtra(ARG_DELETE_FLAG, sharedVM.getDeleteFlag().getValue());
        Log.e("DEBUG", sharedVM.getDeleteFlag().getValue().toString());

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
