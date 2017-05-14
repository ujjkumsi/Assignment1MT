package com.mindtickle.assignment1;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by beyonder on 13/5/17.
 */

public class Assignment1Application extends Application {


    private static Assignment1Application mInstance;
    private static RequestQueue mRequestQueue;
    private static ObjectMapper mapper;


    @Override
    public void onCreate() {

        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);
        mInstance = this;
        mapper = new ObjectMapper();
    }

    public static Assignment1Application getInstance(){
        return mInstance;
    }

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static ObjectMapper getMapper(){
        return mapper;
    }


}

