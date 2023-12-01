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
    public void setContext(Context context) {
        queue = Volley.newRequestQueue(context);
    }
    public void getDescription(String serial, Response.Listener<String> sucsessListener, Response.ErrorListener errorListener){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url + serial,
                sucsessListener,
                errorListener);
        queue.add(stringRequest);
    }
}
