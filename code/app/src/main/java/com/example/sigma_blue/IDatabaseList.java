package com.example.sigma_blue;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Interface that enforces methods that are required for a list to be connected to database
 */
public interface IDatabaseList<T> {
    public void setList(List<T> lst);
    public void updateUI();
    public List<T> loadArray(QuerySnapshot q);
    public void startListening();
}
