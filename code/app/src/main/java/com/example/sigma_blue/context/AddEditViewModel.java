package com.example.sigma_blue.context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.item.Item;

/**
 * Class that manages sharing of the item to be added/edited between all fragment
 */
public class AddEditViewModel extends ViewModel
{
    // Live data instance
    private final MutableLiveData<Item> mItem = new MutableLiveData<>(new Item());
    private final MutableLiveData<Item> mEditItem = new MutableLiveData<>(new Item());
    // TODO: Change String to enum
    private final MutableLiveData<String>  mMode = new MutableLiveData<>();
    private final MutableLiveData<String> mId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDeleteFlag = new MutableLiveData<>();
    private final MutableLiveData<Account> mAccount = new MutableLiveData<>();

    /**
     * Accesses the shared item object
     * @return mItem the LiveData object of the stored item
     */
    public LiveData<Item> getItem()
    {
        return mItem;
    }

    /**
     * Sets the shared item
     * @param item item shared between fragments
     */
    public void setItem(Item item)
    {
        mItem.setValue(item);
    }

    public LiveData<Item> getEditItem()
    {
        return mEditItem;
    }

    public void setEditItem(Item item)
    {
        mEditItem.setValue(item);
    }

    public LiveData<String> getMode() { return mMode; }

    public void setMode(String mode) { mMode.setValue(mode); }

    public LiveData<String> getId() { return mId; }

    public void setId(String id) { mId.setValue(id); }

    public LiveData<Boolean> getDeleteFlag() { return mDeleteFlag; }

    public void setDeleteFlag(Boolean deleteFlag) { mDeleteFlag.setValue(deleteFlag);}

    public LiveData<Account> getAccount() { return mAccount; }

    public void setAccount(Account account) { mAccount.setValue(account);}
}
