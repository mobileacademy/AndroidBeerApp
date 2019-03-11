package com.example.beerapp.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BeerApiRetrofit {

    //https://api.punkapi.com/v2/beers
    @GET("beers")
    Call<List<Beer>> getBeers();

    //ttps://api.punkapi.com/v2/beers/1

    //https://api.punkapi.com/v2/beers/random
}
