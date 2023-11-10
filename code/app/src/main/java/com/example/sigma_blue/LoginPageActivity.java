package com.example.sigma_blue;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;

/**
 * Class for first opened activity of app (login/create account page)
 */
public class LoginPageActivity extends BaseActivity implements CreateAccFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener{

    public Button createAccBtn;
    public Button loginBtn;
    private FragmentLauncher fragmentLauncher;
    private DialogFragment createAccFragment;
    private DialogFragment loginFragment;
    private AccountList userAccountList;

    private Account currentAccount;


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

        createAccBtn = findViewById(R.id.createAccButton);
        loginBtn = findViewById(R.id.loginButton);
        fragmentLauncher = FragmentLauncher.newInstance(this);

        userAccountList = new AccountList();

        createAccBtn.setOnClickListener((v) -> {
            createAccFragment = new CreateAccFragment();
            fragmentLauncher.startFragmentTransaction(createAccFragment, "CREATE_ACCOUNT");
        });

        loginBtn.setOnClickListener((v) -> {
            loginFragment = new LoginFragment().newInstance(userAccountList);
            fragmentLauncher.startFragmentTransaction(loginFragment, "LOGIN");
        });
    }

    /**
     * Method called in CreateAccFragment to close fragment and return new account object
     * @param newAccount
     * This is the new account object that holds the new account information
     */
    @Override
    public void onConfirmPressed(Account newAccount) {
        userAccountList.add(newAccount);
    }

    /**
     * Method called in LoginFragment to close fragment and return new account object
     * @param matches
     * This is a boolean that is the result of a check if the user input matched the desired login information (true = login info matches)
     */
    @Override
    public void onLoginPressed(boolean matches, Account account){
        if (matches) {
            Intent intent = new Intent(LoginPageActivity.this, ViewListActivity.class);
            intent.putExtra("account", account);
            startActivity(intent);
        }
        else {
            loginFragment = new LoginFragment().newInstance(userAccountList);
            fragmentLauncher.startFragmentTransaction(loginFragment, "LOGIN");
        }
    }
}
