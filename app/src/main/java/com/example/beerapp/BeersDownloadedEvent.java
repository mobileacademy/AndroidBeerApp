package com.example.beerapp;

import com.example.beerapp.networking.Beer;

import java.util.ArrayList;
import java.util.List;

public class BeersDownloadedEvent {

    private List<Beer> beers = new ArrayList<>();

    public BeersDownloadedEvent(List<Beer> list) {
        beers.addAll(list);
    }

    public List<Beer> getBeers() {
        return beers;
    }
}
