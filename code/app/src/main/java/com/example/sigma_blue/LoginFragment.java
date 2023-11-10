package com.example.sigma_blue;

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

import org.checkerframework.checker.units.qual.A;

/**
 * Class for login version of fragment from main login page
 */
public class LoginFragment extends DialogFragment {

    private EditText usernameInput;
    private EditText passwordInput;
    private OnFragmentInteractionListener listener;

    /**
     * Listens for interaction with confirm button in fragment, contains method to return to
     * original activity
     */
    public interface OnFragmentInteractionListener {
        void onLoginPressed(boolean matches, Account account);
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

        // views for getting user input
        usernameInput = view.findViewById(R.id.usernameEditText);
        passwordInput = view.findViewById(R.id.passwordEditText);

        // creates the account that the user input will be tested against
        Bundle args = getArguments();
        AccountList validAccounts;
        validAccounts = (AccountList) args.getSerializable("accountList");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /*
                         * Note: the way the fragment checks the user input against existing
                         * accounts will need to be changed later on as this only works with one
                         * account object existing for the app
                         */

                        // gets user input
                        String username = usernameInput.getText().toString();
                        String password = passwordInput.getText().toString();

                        Account enteredAccount = new Account(username, password);
                        // checks if user input matches test account
                        assert validAccounts != null;
                        boolean matches = validAccounts.contains(enteredAccount);

                        // creates popup message for incorrect user account information input
                        if (!matches) {
                            Toast incorrectMessage = Toast
                                    .makeText(getActivity(),
                                            getResources().getString(R.string
                                                    .invalid_username_password),
                                            Toast.LENGTH_SHORT);
                            incorrectMessage.show();
                        }

                        listener.onLoginPressed(matches, enteredAccount);
                    }
                }).create();
    }

}
