package com.example.sigma_blue.entity.description;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Asks upcitemdb for a description
 * see https://google.github.io/volley/simple.html
 */
public class DescriptionDB {
    private final static String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=";
    RequestQueue queue;
    public DescriptionDB() {
    }

    /**
     * I do not like how context is needed here, but this is the best library I could find for this
     * @param context context of calling activity/fragment
     */
    public void setContext(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Start fetching a description for an object
     * @param serial product code to search
     * @param successListener listener to call on a successful download
     * @param errorListener listener to call on fail
     */
    public void getDescription(String serial, Response.Listener<String> successListener, Response.ErrorListener errorListener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url + serial,
                successListener,
                errorListener);
        queue.add(stringRequest);
    }
}
