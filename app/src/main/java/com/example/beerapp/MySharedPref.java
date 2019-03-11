package com.example.beerapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    private SharedPreferences sharedPreferences;
    private static final  String PREF_NAME = "app_shared_pref";

    public MySharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addStringToSharedPref(String key, String value) {
        // get editor object

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);

        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void remove(String key) {
        sharedPreferences.edit().remove(key).commit();
    }

}
