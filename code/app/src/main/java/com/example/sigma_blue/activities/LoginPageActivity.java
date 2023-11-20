package com.example.sigma_blue.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.R;
import com.example.sigma_blue.fragments.CreateAccFragment;
import com.example.sigma_blue.fragments.FragmentLauncher;
import com.example.sigma_blue.fragments.LoginFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Class for first opened activity of app (login/create account page)
 */
public class LoginPageActivity extends BaseActivity implements CreateAccFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener{

    public Button createAccBtn;
    public Button loginBtn;
    private FragmentLauncher fragmentLauncher;
    private DialogFragment createAccFragment;
    private DialogFragment loginFragment;
    private GlobalContext globalContext;


    /**
     * Entry method.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *                           <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        globalContext = GlobalContext.getInstance();

        createAccBtn = findViewById(R.id.createAccButton);
        loginBtn = findViewById(R.id.loginButton);
        fragmentLauncher = FragmentLauncher.newInstance(this);

        createAccBtn.setOnClickListener((v) -> {
            createAccFragment = new CreateAccFragment();
            globalContext.newState("create_account_fragment");
            fragmentLauncher.startFragmentTransaction(createAccFragment, "CREATE_ACCOUNT");
        });

        loginBtn.setOnClickListener((v) -> {
            globalContext.newState("login_fragment");
            loginFragment = new LoginFragment().newInstance(globalContext.getAccountList());
            fragmentLauncher.startFragmentTransaction(loginFragment, "LOGIN");
        });
    }

    /**
     * Method called in CreateAccFragment to close fragment
     */
    @Override
    public void onConfirmPressed(boolean successful) {
        Snackbar confirmSnackbar;
        if (successful) {
            confirmSnackbar = Snackbar.make(findViewById(R.id.login_main), "Account Created", Snackbar.LENGTH_LONG);
        } else {
            confirmSnackbar = Snackbar.make(findViewById(R.id.login_main), "Account with that username already exists.", Snackbar.LENGTH_LONG);
        }
        confirmSnackbar.show();
    }

    /**
     * Method called in LoginFragment to close fragment. Brings the login page back up with an error
     * if the password/user is wrong.
     * @param matches
     * This is a boolean that is the result of a check if the user input matched the desired login information (true = login info matches)
     */
    @Override
    public void onLoginPressed(boolean matches){
        if (matches) {
            Intent intent = new Intent(LoginPageActivity.this, ViewListActivity.class);
            globalContext.newState("list_view_activity");
            startActivity(intent);
        }
        else {
            // creates popup message for incorrect user account information input
            Snackbar incorrectMessage = Snackbar.make(findViewById(R.id.login_main), getResources().getString(R.string.invalid_username_password), Snackbar.LENGTH_LONG);
            incorrectMessage.show();

            //comment this out so it's easier to make a new account after a failed login
//            loginFragment = new LoginFragment().newInstance(globalContext.getAccountList());
//            globalContext.newState("login_fragment");
//            fragmentLauncher.startFragmentTransaction(loginFragment, "LOGIN");
        }
    }
}