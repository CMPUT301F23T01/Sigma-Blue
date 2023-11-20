package com.example.sigma_blue.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;

/**
 * Fragment class for the dialog fragment. Controls the UI element and
 * communicate with the backend.
 */
public class QueryFragment extends DialogFragment {

    /**
     * ViewHolder design pattern for better encapsulation of the UI elements
     */
    private class ViewHolder {
        Button backButton;

        /**
         * Needs the parent view to be inflated before this class can be
         * constructed
         * @param entireView is the parent view (dialog box fragment)
         */
        public ViewHolder(View entireView) {
            backButton = entireView.findViewById(R.id.query_cancel_button);
        }

        /**
         * Method sets up the UI interactions.
         */
        public void setUIListeners() {
            backButton.setOnClickListener(view -> dismiss());   // Go back
        }
    }

    private ViewHolder viewHolder;          // The view holder

    /**
     * Empty public constructor
     */
    public QueryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Override for the onCreateView. This lets us use the inflater that is
     * provided by android instead of making a new one.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstance If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View fragmentView = inflater.inflate(R.layout.sort_filter_fragment,
                container, false);

        viewHolder = new ViewHolder(fragmentView);

        viewHolder.setUIListeners();

        return fragmentView;
    }

}
