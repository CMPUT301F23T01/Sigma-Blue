package com.example.sigma_blue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sigma_blue.fragments.ItemDetailsFragment;
import com.example.sigma_blue.fragments.ItemPhotosFragment;
import com.example.sigma_blue.fragments.ItemTagsFragment;

/**
 * Class for managing custom fragments inside of a ViewPager2 layout
 */
public class ViewPagerAdapter extends FragmentStateAdapter
{
    private final TabMode mode;
    private ItemDetailsFragment itemDetailsFragment;
    private ItemPhotosFragment itemPhotosFragment;
    private ItemTagsFragment itemTagsFragment;
    private boolean fragmentsInitialized = false;
    private final String ERROR_MESSAGE = "Position must correspond to a fragment";

    public ViewPagerAdapter(@NonNull Fragment fragment, TabMode mode) {
        super(fragment);
        this.mode = mode;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                itemDetailsFragment = new ItemDetailsFragment(mode);
                fragment = itemDetailsFragment;
                break;
            case 1:
                itemTagsFragment = new ItemTagsFragment(mode);
                fragment = itemTagsFragment;
                break;
            case 2:
                itemPhotosFragment = new ItemPhotosFragment(mode);
                fragment = itemPhotosFragment;
                break;
            default:
                throw new RuntimeException(ERROR_MESSAGE);
        }
        fragmentsInitialized = true;
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /**
     * Verify that any required edit text fields are filled before switching tabs
     * @return boolean whether the required edit text fields are filled
     */
    public boolean verifyDetailsText() {
        return itemDetailsFragment.verifyText();
    }

    /**
     * Save the edit text fields to the global context
     */
    public void saveTextToContext()
    {
        itemDetailsFragment.saveText();
    }

    /**
     * Calls view of current selected tab to update list contents to the global context
     * @param position of tab selected
     */
    public void updateFromContext(int position)
    {
        if (fragmentsInitialized) {
            switch (position) {
                case 0: {
                    itemDetailsFragment.updateText();
                    break;
                }
                case 1: {
                    itemTagsFragment.updateTags();
                    break;
                }
                case 2: {
                    itemPhotosFragment.updateImageList();
                    break;
                }
                default: {
                    throw new RuntimeException(ERROR_MESSAGE);
                }
            }
        }
    }
}
