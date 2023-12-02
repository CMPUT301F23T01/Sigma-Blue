package com.example.sigma_blue.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmDelete {

    // method for generating a confirm delete menu, takes in an OnClickListener so that
    // different implementations of deletion can be used. This is to be defined by classes
    // that us the implementation
    public static void confirmDelete(Context context, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Please confirm deletion.");
        builder.setPositiveButton("Confirm", (DialogInterface.OnClickListener) confirmListener);
        builder.setNegativeButton("Cancel", (dialog, which) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}