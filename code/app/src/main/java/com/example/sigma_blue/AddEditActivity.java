package com.example.sigma_blue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import java.util.Objects;

public class AddEditActivity extends AppCompatActivity
{
    // TODO: Add these modes to a global enum file
    private static final String ARG_ITEM = "item"; // item key accessor
    private static final String ARG_MODE = "mode";
    private Bundle bundledItem; // item to add/edit
    private String newItemFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set xml view
        setContentView(R.layout.add_edit_activity);

        // Load bundle
        if (savedInstanceState == null)
        {
            bundledItem = getIntent().getExtras();
            newItemFlag = bundledItem.getString(ARG_MODE);
        }
        else
        {
            bundledItem = savedInstanceState;
            newItemFlag = bundledItem.getString(ARG_MODE);
        }

        // Setup nav controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        NavGraph graph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        if (Objects.equals(newItemFlag, "add"))
        {
            graph.setStartDestination(R.id.editFragment);
        }
        else
        {
            graph.setStartDestination(R.id.detailsFragment);
        }
        navController.setGraph(graph, bundledItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
