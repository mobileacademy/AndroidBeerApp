package com.example.beerapp;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("myapplication", "onCreate");


    }

}
