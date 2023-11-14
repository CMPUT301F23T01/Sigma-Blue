package com.example.sigma_blue.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Class for login version of fragment from main login page
 */
public class LoginFragment extends DialogFragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private OnFragmentInteractionListener listener;
    private GlobalContext globalContext;

    /**
     * Listens for interaction with confirm button in fragment, contains method to return to
     * original activity
     */
    public interface OnFragmentInteractionListener {
        void onLoginPressed(boolean matches);
    }

    /**
     * Creates instance to take in account object from LoginPageActivity so it can be checked
     * against user input
     */
    public static LoginFragment newInstance(AccountList accountList) {

        Bundle args = new Bundle();
        args.putSerializable("accountList", accountList);

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.login_page_fragment, null);

        globalContext = GlobalContext.getInstance();
        // views for getting user input
        usernameInput = view.findViewById(R.id.usernameEditText);
        passwordInput = view.findViewById(R.id.passwordEditText);

        // creates the account that the user input will be tested against

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // gets user input
                        String username = usernameInput.getText().toString();
                        String password = passwordInput.getText().toString();

                        Account enteredAccount = new Account(username, password);
                        // checks if user input matches test account
                        boolean matches = globalContext.getAccountList().contains(enteredAccount);

                        // creates popup message for incorrect user account information input
                        if (!matches) {
                            Toast incorrectMessage = Toast
                                    .makeText(getActivity(),
                                            getResources().getString(R.string
                                                    .invalid_username_password),
                                            Toast.LENGTH_SHORT);
                            incorrectMessage.show();
                        } else {
                            globalContext.login(enteredAccount);
                        }
                        globalContext.newState("login_activity");
                        listener.onLoginPressed(matches);
                    }
                }).create();
    }

}
