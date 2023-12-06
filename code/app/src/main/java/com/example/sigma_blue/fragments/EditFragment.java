package com.example.sigma_blue.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.adapter.TabMode;
import com.example.sigma_blue.adapter.TabSelected;
import com.example.sigma_blue.adapter.ViewPagerAdapter;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;
import com.example.sigma_blue.databinding.EditFragmentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.common.base.VerifyException;

/**
 * Class for handling fragment for editing an Item objects values
 */
public class EditFragment extends Fragment
{
    private final GlobalContext globalContext = GlobalContext.getInstance();
    // Fragment binding
    private EditFragmentBinding binding;

    // Fragment ui components
    private EditText textName;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabSelected tabSelected;

    /**
     * Required empty public constructor
     */
    public EditFragment() {
    }

    /**
     * Method to create the activity
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method to inflate layout of fragment and bind components
     * @param inflater is the LayoutInflater that is going to inflate for the fragment
     * @param container is a ViewGroup of the views for the fragment
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = EditFragmentBinding.inflate(inflater, container, false);

        // bind ui components
        textName = binding.getRoot().findViewById(R.id.text_name_disp);

        tabLayout = binding.getRoot().findViewById(R.id.tabLayout);
        viewPager = binding.getRoot().findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, TabMode.Edit);

        // TODO add buttons here
        return binding.getRoot();
    }

    /**
     * Method to set details of item in fragment and handle button interactions
     * @param view is the View of the fragment
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final AddEditActivity activity = (AddEditActivity) requireActivity();

        // Initialize tab layout ui
        viewPager.setAdapter(viewPagerAdapter);
        tabSelected = TabSelected.Details;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (tabSelected == TabSelected.Details) {
                    if (viewPagerAdapter.verifyDetailsText()) {
                        tabSelected = TabSelected.of(position);
                        viewPagerAdapter.saveTextToContext();
                        viewPager.setCurrentItem(position);
                    }
                    else { tabLayout.getTabAt(tabSelected.position()).select(); }
                }
                else {
                    tabSelected = TabSelected.of(position);
                    viewPager.setCurrentItem(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                globalContext.setModifiedItem(null);
                if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
                    // Cancel new item; Return to ViewListActivity
                    globalContext.setCurrentItem(null);
                    globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                    activity.returnAndClose();

                } else {
                    globalContext.getImageManager().updateFromItem(globalContext.getCurrentItem());
                    // Navigate to Item Details
                    NavHostFragment.findNavController(EditFragment.this)
                            .navigate(R.id
                                    .action_editFragment_to_detailsFragment);
                }
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // Load ui text and save into shared item; Navigate to DetailsFragment
                boolean verified = true;
                if (tabSelected == TabSelected.Details) {
                    verified = viewPagerAdapter.verifyDetailsText() && verifyName();
                }

                if (verified) {
                    // need a new item as to not overwrite the old one. If the
                    // old one is overwritten then we don't know which item in
                    // the list needs to be deleted if doing an edit.
                    Item oldItem = globalContext.getCurrentItem();
                    if (tabSelected == TabSelected.Details) {
                        viewPagerAdapter.saveTextToContext();
                    }
                    Item newItem = globalContext.getModifiedItem();
                    loadTextName(newItem);

                    // State control for adding items
                    if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
                        if (globalContext.getItemList().getList().contains(newItem)) {
                            Snackbar errorSnackbar = Snackbar.make(v, "Item Already Exists", Snackbar.LENGTH_LONG);
                            errorSnackbar.show();
                        } else {
                            globalContext.getItemList().add(newItem);
                            globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                            activity.returnAndClose();
                        }
                    } else if (globalContext.getCurrentState() == ApplicationState.EDIT_ITEM_FRAGMENT) {
                        if (globalContext.getItemList().getList().contains(newItem) && !newItem.equals(oldItem)){
                            Snackbar errorSnackbar = Snackbar.make(v, "Item Already Exists", Snackbar.LENGTH_LONG);
                            errorSnackbar.show();
                        } else {
                            globalContext.getItemList().updateEntity(newItem, oldItem);
                            globalContext.setCurrentItem(newItem);
                            globalContext.newState(ApplicationState.DETAILS_FRAGMENT);
                            NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
                        }
                    } else {
                        Log.e("BAD STATE",
                                "Edit and the item doesn't exist");
                        throw new VerifyException("Bad state"); // Unhandled
                    }
                }
            }
        });
        // suboptimal and a bit ugly
        textName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                globalContext.getModifiedItem().setName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        textName.setText(globalContext.getModifiedItem().getName());
        viewPagerAdapter.updateFromContext(tabSelected.position());
    }

    /**
     * Method for destroying fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Verifies that the item name is not empty when saving edits
     * @return flag verifying that required EditText's are populated
     */
    private boolean verifyName() {
        String emptyErrText = "Must enter a value before saving";
        if (TextUtils.isEmpty(textName.getText()))
        {
            textName.setError(emptyErrText);
            return false;
        }
        else { return true; }
    }

    /**
     * Adds ui text into an item object
     * @param item to edit
     */
    private void loadTextName(@NonNull Item item)
    {
        item.setName(textName.getText().toString());
    }
}
