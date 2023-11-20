package com.example.sigma_blue.database;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Interface that enforces methods that are required for an Object to be connected to database
 */
public interface IDatabaseItem<T> {
    public String getDocID();
    public Function<IDatabaseItem<T>, HashMap<String, Object>> getHashMapOfEntity();
}
