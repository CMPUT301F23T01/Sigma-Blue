package com.example.sigma_blue.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

/**
 * Add/Edit activity class. Controls the add and edit activity fragments. Very little happens here,
 * see DetailsFragment and Edit Fragment.
 */
public class AddEditActivity extends AppCompatActivity
{
    private GlobalContext globalContext;
    private NavGraph graph;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set xml view
        setContentView(R.layout.add_edit_activity);

        globalContext = GlobalContext.getInstance();

        // Setup nav controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        graph = navController.getNavInflater().inflate(R.navigation.nav_graph);

        //depending on the requested state go the the correct fragment
    }

    @Override
    public void onResume() {
        super.onResume();
        if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
            graph.setStartDestination(R.id.editFragment);
        } else if (globalContext.getCurrentState() == ApplicationState
                .MULTI_SELECT_TAG_MANAGER_FRAGMENT) {
            graph.setStartDestination(R.id.tagManagerFragment);
        } else if (globalContext.getCurrentState() == ApplicationState
                .DETAILS_FRAGMENT) {
            graph.setStartDestination(R.id.detailsFragment);
        } else if (Objects.equals(globalContext.getCurrentState(), ApplicationState.EDIT_ITEM_FRAGMENT)) {
            graph.setStartDestination(R.id.editFragment);
        } else {
            Log.e("DEBUG", "Bad AddEditMode");
            return;
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
