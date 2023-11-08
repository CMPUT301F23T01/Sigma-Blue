package com.example.sigma_blue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AddEditActivity extends AppCompatActivity
{
    private static final String ARG_ITEM = "item"; // item key accessor
    Bundle bundledItem; // item to add/edit

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
        }
        else
        {
            bundledItem = savedInstanceState;
        }

        // Setup nav controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_add_edit_activity);
        navController.setGraph(R.navigation.nav_graph, bundledItem);
    }

//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//
//        // Get bundled item from fragment and pass it to ViewList
//        bundledItem = getIntent().getExtras();
//        Intent intent = new Intent(AddEditActivity.this, ViewListActivity.class);
//        intent.putExtra(ARG_ITEM, bundledItem);
//        setResult(Activity.RESULT_OK, intent);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
