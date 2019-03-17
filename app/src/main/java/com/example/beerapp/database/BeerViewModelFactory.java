package com.example.beerapp.database;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class BeerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;

    public BeerViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BeerViewModel(application);
    }
}
