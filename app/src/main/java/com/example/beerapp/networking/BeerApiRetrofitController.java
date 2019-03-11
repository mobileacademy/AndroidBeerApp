package com.example.beerapp.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeerApiRetrofitController {

    private final static String BASE_URL = "https://api.punkapi.com/v2/";

    private BeerApiRetrofit beerApi;

    private static Retrofit retrofit = null;

    public void connect() {

        OkHttpClient client = new OkHttpClient.Builder().build();

        Gson gson = new GsonBuilder().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        beerApi = retrofit.create(BeerApiRetrofit.class); // interface with all endpoints
    }

    public void getListofBeers(Callback<List<Beer>> responseCallback) {
        Call<List<Beer>> call = beerApi.getBeers();

        call.enqueue(responseCallback);
    }

}
