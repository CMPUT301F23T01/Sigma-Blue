package com.example.sigma_blue.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.R;

/**
 * Class for handling the create account version of fragment from main login page
 */
public class CreateAccFragment extends DialogFragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private OnFragmentInteractionListener listener;

    /**
     * Listens for interaction with confirm button in fragment, contains method to return to original activity
     */
    public interface OnFragmentInteractionListener {
        void onConfirmPressed(Account account);
    }

    /**
     * Identifies listener of current fragment from context, errors if correct interface is not implemented
     * @param context is a Context object of the current enivronment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Method on creation of fragment
     * @param savedInstanceState
     * @return builder is a Dialog object for creating fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.login_page_fragment, null);

        // views for getting user input
        usernameInput = view.findViewById(R.id.usernameEditText);
        passwordInput = view.findViewById(R.id.passwordEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Create Account")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // gets user input
                        String username = usernameInput.getText().toString();
                        String password = passwordInput.getText().toString();

                        // creates new account for user
                        Account userAccount = new Account(username, password);

                        listener.onConfirmPressed(userAccount);
                    }
                }).create();
    }

}
