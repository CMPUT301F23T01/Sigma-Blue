package com.example.sigma_blue.placeholder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public interface ConfirmDelete {

    // method for generating a confirm delete menu, takes in an OnClickListener so that
    // different implementations of deletion can be used. This is to be defined by classes
    // that us the implementation
    default void confirmDelete(Context context, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Please confirm deletion. Cancel if deletion is not needed/intended");
        builder.setPositiveButton("Confirm", (DialogInterface.OnClickListener) confirmListener);
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
