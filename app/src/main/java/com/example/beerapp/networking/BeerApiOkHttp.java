package com.example.beerapp.networking;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BeerApiOkHttp {

    private static OkHttpClient httpClient;
    private static final String BASE_URL = "https://api.punkapi.com/v2/";
    private static final String BEERS_URL = "beers";

    static OkHttpClient getInstance() {
        if (httpClient == null) {
            httpClient = new OkHttpClient();
        }
        return httpClient;
    }

    public static List<Beer> retriveBeersSync() throws IOException {
        List<Beer> results = new ArrayList<>();

        String url = BASE_URL + BEERS_URL;

        Request getRequest = new Request.Builder().url(url).build();

        Response response = getInstance().newCall(getRequest).execute();

        Log.d("service", "@@@resp " + response.isSuccessful());

        if (response.isSuccessful() && response.body() != null) {
            String respBody = response.body().string();

            Gson gson = new GsonBuilder().create();

            try {
                JSONArray jsonArray = new JSONArray(respBody);

                for (int i=0;i<jsonArray.length();i++) {

                    String beerString = jsonArray.getString(i);

                    Beer newBeer = gson.fromJson(beerString, Beer.class);

                    results.add(newBeer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return results;
    }


    public static void retrieveBeersAsync(Callback responseCallback) {
        String url = BASE_URL + BEERS_URL;

        Request getRequest = new Request.Builder().url(url).build();

        getInstance().newCall(getRequest).enqueue(responseCallback);

    }



}
