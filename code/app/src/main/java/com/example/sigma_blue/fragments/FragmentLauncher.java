package com.example.sigma_blue.fragments;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Based on https://developer.android.com/guide/fragments/fragmentmanager.
 * This class encapsulates fragment launching related methods. The use this class, embed it into
 * the class of the activity or fragment that is calling it.
 */
public class FragmentLauncher {
    /* The fragment manager is an object that both activities and fragments share. Holds the current
    * one */
    private FragmentManager hostFragmentManager;

    /* Factories and constructors */

    /**
     * Factory method for embedding on to an Activity
     * @param host is a FragmentActivity class (supers most activities) that is hosting this object.
     * @return the FragmentLauncher object that holds the FragmentManger of the host.
     */
    public static FragmentLauncher newInstance(FragmentActivity host) {
        return new FragmentLauncher(host.getSupportFragmentManager());
    }

    /**
     * Factory method for embedding on to a fragment
     * @param host is the Fragment that is hosting the fragment launcher.
     * @return the FragmentLauncher object that holds the FragmentManger of the host.
     */
    public static FragmentLauncher newInstance(Fragment host) {
        return new FragmentLauncher(host.getChildFragmentManager());
    }

    /**
     * Constructor
     * @param hostFragMan is a FragmentManager of the host that is launching fragments.
     */
    public FragmentLauncher(FragmentManager hostFragMan) {
        this.hostFragmentManager = hostFragMan;
    }

    /**
     * DialogFragment polymorphism for launching fragments. This assumes that we have already
     * placed our initial data in the form of a Bundle when we pass @code{fragment}.
     * @param fragment the dialog fragment that is being launched.
     * @param tag is needed for dialog fragment. (Need to read what it does).
     */
    public void startFragmentTransaction(DialogFragment fragment, String tag) {
        fragment.show(hostFragmentManager, tag);
    }

    public void startFragmentTransaction(Fragment fragment, String tag) {
        ;
    }

}
