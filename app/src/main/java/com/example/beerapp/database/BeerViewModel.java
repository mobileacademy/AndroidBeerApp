package com.example.beerapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.beerapp.BeersDownloadedEvent;
import com.example.beerapp.networking.Beer;
import com.example.beerapp.networking.BeerApiRetrofitController;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BeerViewModel extends AndroidViewModel {

    // used to post new data by developer using  beerList.postValue(response.body());
    private MutableLiveData<List<Beer>> beerList;

    // used to post new data by room when changing data from the beerEntity table
    private LiveData<List<BeerEntity>> list;

    public BeerViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Beer>> getBeerList() {
        if (beerList == null) {

            beerList = new MutableLiveData<>();

            BeerApiRetrofitController controller = new BeerApiRetrofitController();
            controller.connect();

            controller.getListofBeers(new retrofit2.Callback<List<Beer>>() {
                @Override
                public void onResponse(retrofit2.Call<List<Beer>> call, retrofit2.Response<List<Beer>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // new data available
                        beerList.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<Beer>> call, Throwable t) {
                    Log.e("service", "error getting list of beers");
                }
            });
        }

        return beerList;
    }

    public LiveData<List<BeerEntity>> getLiveBeerList() {
        AppDatabase db = AppDatabase.getDatabase(getApplication());

        list = db.beerDao().getLiveDataList();

        return list;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        beerList = null;
    }
}
