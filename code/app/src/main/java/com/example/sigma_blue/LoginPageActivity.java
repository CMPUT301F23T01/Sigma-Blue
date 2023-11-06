package com.example.sigma_blue;

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
    private Account userAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_activity);

        createAccBtn = findViewById(R.id.createAccButton);
        loginBtn = findViewById(R.id.loginButton);
        fragmentLauncher = FragmentLauncher.newInstance(this);

        createAccBtn.setOnClickListener((v) -> {
            createAccFragment = new CreateAccFragment();
            fragmentLauncher.startFragmentTransaction(createAccFragment, "CREATE_ACCOUNT");
        });

        loginBtn.setOnClickListener((v) -> {
            Account inputAcc = userAccount;
            loginFragment = new LoginFragment().newInstance(inputAcc);
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
        userAccount = newAccount;
    }

    /**
     * Method called in LoginFragment to close fragment and return new account object
     * @param matches
     * This is a boolean that is the result of a check if the user input matched the desired login information (true = login info matches)
     */
    @Override
    public void onLoginPressed(boolean matches){
        if (matches) {
            // this is where the next activity (ViewListActivity) is going to be launched
        }
        else {
            Account inputAcc = userAccount;
            loginFragment = new LoginFragment().newInstance(inputAcc);
            fragmentLauncher.startFragmentTransaction(loginFragment, "LOGIN");
        }
    }
}
