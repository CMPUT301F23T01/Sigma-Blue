package com.example.sigma_blue;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sigma_blue.databinding.AddEditActivityBinding;

public class AddEditActivity extends AppCompatActivity
{
    private AppBarConfiguration appBarConfiguration;
    private static final String ARG_ITEM = "item"; // key for accessing item
    Bundle currentItem; // item to add/edit

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set xml view
        setContentView(R.layout.add_edit_activity);

        // Load bundle
        if (savedInstanceState == null)
        {
            currentItem = getIntent().getExtras();
        }
        else
        {
            currentItem = savedInstanceState;
        }

        // Setup nav controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        navController.setGraph(R.navigation.nav_graph, currentItem);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
