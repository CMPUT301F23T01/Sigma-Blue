package com.example.sigma_blue.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

/**
 * Add/Edit activity class. Controls the add and edit activity.
 */
public class AddEditActivity extends BaseActivity
{
    private GlobalContext globalContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set xml view
        setContentView(R.layout.add_edit_activity);

        globalContext = GlobalContext.getInstance();

        // Setup nav controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        NavGraph graph = navController.getNavInflater().inflate(R.navigation.nav_graph);

        //depending on the requested state go the the correct fragment
        if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
            graph.setStartDestination(R.id.editFragment);
        } else if (globalContext.getCurrentState() == ApplicationState
                .MULTI_SELECT_TAG_MANAGER_FRAGMENT) {
            graph.setStartDestination(R.id.tagManagerFragment);
        } else if (globalContext.getCurrentState() == ApplicationState
                .DETAILS_FRAGMENT) {
            graph.setStartDestination(R.id.detailsFragment);
        } else {
            throw new RuntimeException("Wrong add/edit activity state");
        }
        navController.setGraph(graph);
    }

    /**
     * return item back to ViewListActivity and close activity
     */
    public void returnAndClose() {
        Intent i = new Intent(this, ViewListActivity.class);

        globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
