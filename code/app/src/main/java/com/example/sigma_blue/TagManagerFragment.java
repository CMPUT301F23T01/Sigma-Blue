package com.example.sigma_blue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TagManagerFragment extends DialogFragment {
    private TagList tags;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // TODO Look into multiple item selection for the tag manager.
        // NOTE You may have to use an adapter.


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.tag_manager_fragment, container, false);
    }
}
