package com.example.sigma_blue.entity.description;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sigma_blue.entity.item.Item;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DescriptionManager {
    private HashMap<String, String> descriptionMappings;
    private static boolean DBEnabled = true; // we have limited queries allowed (100/day)
    private DescriptionDB descriptionDB;
    public DescriptionManager() {
        descriptionMappings = new HashMap<>();
        descriptionDB = new DescriptionDB();
        addTestData();
    }

    /**
     * Adds a few test examples
     */
    public void addTestData() {
        descriptionMappings.put("", "");
    }
    public void setContext(Context context){
        descriptionDB.setContext(context);
    }

    /**
     * Update the description of an item from the serial number.
     * @param serial serial number to lookup
     * @param successListener called to update ui items on a success. listener is passed the description string
     * @param errorListener called on an error.
     */
    public void updateItemDescription(String serial, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        if (descriptionMappings.containsKey(serial)) {
            successListener.onResponse(descriptionMappings.get(serial));
        } else if (DBEnabled){
            successListener.onResponse("Loading...");
            descriptionDB.getDescription(
                serial,
                response -> {
                    // wrap the given listener to cache the response
                    String parsed = parseResponse(response);
                    descriptionMappings.put(serial, parsed);
                    successListener.onResponse(parsed);
                },
                errorListener);
        } else {
            errorListener.onErrorResponse(null);
        }
    }

    private String parseResponse(String s) {
        try {
            JSONObject json = new JSONObject(s);

            String description = (String) ((JSONObject)json.getJSONArray("items").get(0)).get("description");
            String name = (String) ((JSONObject)json.getJSONArray("items").get(0)).get("title");
            return name + ": " + description;
        } catch (Exception e) {
            Log.e("DESCRIPTION DOWNLOAD", "Failed to parse json");
            Log.e("DESCRIPTION DOWNLOAD", e.toString());
            return "";
        }
    }
}
