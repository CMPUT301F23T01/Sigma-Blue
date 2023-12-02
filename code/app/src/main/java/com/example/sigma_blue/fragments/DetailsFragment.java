package com.example.sigma_blue.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sigma_blue.R;
import com.example.sigma_blue.adapter.TabMode;
import com.example.sigma_blue.adapter.TabSelected;
import com.example.sigma_blue.adapter.ViewPagerAdapter;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.databinding.DetailsFragmentBinding;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.placeholder.ConfirmDelete;
import com.google.android.material.tabs.TabLayout;

/**
 * Class for handling activity to view details of an item
 */
public class DetailsFragment extends Fragment implements ConfirmDelete
{
    private GlobalContext globalContext = GlobalContext.getInstance();

    // Fragment binding
    private DetailsFragmentBinding binding;

    // Fragment ui components
    private TextView textName;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabSelected tabSelected;


    /**
     * Required empty public constructor
     */
    public DetailsFragment() {
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
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = DetailsFragmentBinding.inflate(inflater, container, false);

        // bind ui components
        textName = binding.getRoot().findViewById(R.id.text_name_disp);

        tabLayout = binding.getRoot().findViewById(R.id.detailsTabLayout);
        viewPager = binding.getRoot().findViewById(R.id.detailsViewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, TabMode.Details);

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

        globalContext = GlobalContext.getInstance();
        final Item currentItem = globalContext.getCurrentItem();

        // set item details from global context
        textName.setText(currentItem.getName());

        // Initialize tab layout ui
        viewPager.setAdapter(viewPagerAdapter);
        tabSelected = TabSelected.Details;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabSelected = TabSelected.of(position);
                viewPager.setCurrentItem(position);
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

        view.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Navigate to EditFragment
                globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.action_detailsFragment_to_editFragment);
            }
        });

        view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               // method for confirm delete menu, creates onClickListener for specific method of deleting
                confirmDelete(getActivity(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // code for deleting that is to be run if delete is confirmed by user

                        // Return to ViewListActivity; notify object needs to be deleted
                        globalContext.getItemList().remove(currentItem);
                        globalContext.setCurrentItem(null);
                        globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                        activity.returnAndClose();
                    }
                });
            }
        });

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Return to ViewListActivity
                globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                activity.returnAndClose();
            }
        });
    }

    /**
     * Method for destroying fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
