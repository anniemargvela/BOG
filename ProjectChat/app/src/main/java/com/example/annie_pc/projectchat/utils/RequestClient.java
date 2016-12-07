package com.example.annie_pc.projectchat.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestClient {

    private static RequestClient instance;
    private static Context contextInstance;
    private RequestQueue requestQueue;

    private RequestClient(Context context) {
        contextInstance = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestClient getInstance(Context context) {
        if (instance == null) {
            instance = new RequestClient(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(contextInstance.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
