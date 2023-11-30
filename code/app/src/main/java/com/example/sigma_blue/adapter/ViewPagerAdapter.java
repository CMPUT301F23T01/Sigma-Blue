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

    public boolean verifyDetailsText() {
        return itemDetailsFragment.verifyText();
    }

    public void saveToContext(int position)
    {
        switch (position) {
            case 0: {
                itemDetailsFragment.saveText();
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                break;
            }
            default: {
                throw new RuntimeException(ERROR_MESSAGE);
            }
        }
    }

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
