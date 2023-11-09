package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class AccountDB extends ADatabaseHandler<Account> {

    private CollectionReference accountPointer;
    private List<Account> accounts;
    public Function<Account, HashMap<String, String>> hashMapConverter = v ->
    {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("USERNAME", v.getUsername());
        ret.put("PASSWORD", v.getPassword());
        return ret;
    };

    public static AccountDB newInstance() {
        return new AccountDB();
    }

    private AccountDB() {
        accountPointer = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName());
    }
    @Override
    public void add(Account item) {
        addDocument(accountPointer, item, hashMapConverter, item.getDocID());
    }

    @Override
    public void remove(final Account item) {
        removeDocument(accountPointer, item);
    }

    @Override
    public CollectionReference getCollectionReference() {
        return this.accountPointer;
    }

}
