package com.example.sigma_blue;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface IDatabaseList<T> {
    public void setList(List<T> lst);
    public void updateUI();
    public List<T> loadArray(QuerySnapshot q);
    public void startListening();
}
