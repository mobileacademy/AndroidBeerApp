package com.example.beerapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.beerapp.database.AppDatabase;
import com.example.beerapp.database.BeerEntity;
import com.example.beerapp.networking.Beer;
import com.example.beerapp.networking.BeerApiOkHttp;
import com.example.beerapp.networking.BeerApiRetrofitController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GetBeersService extends IntentService {

    public GetBeersService() {
        super("GetBeersService");
    }

    public static final String SYNC_ACTION = "sync_action";
    public static final String ASYNC_ACTION = "async_action";
    public static final String RETROFIT_GET_ACTION = "retrofit_get_action";

    @Override
    protected void onHandleIntent(Intent intent) {

        // on background thread
        if (intent != null) {

            String action = intent.getAction();
            if (action != null) {
                if (action.equals(SYNC_ACTION)){
                    try {
                        List<Beer> list = BeerApiOkHttp.retriveBeersSync();
                        Log.d("service", "list size = " + list.size());


                        for (int i=0; i< list.size(); i++) {
                            Beer beer = list.get(i);
                            BeerEntity entity = new BeerEntity(String.valueOf(beer.getId()), beer.getName(), beer.getDesc(), beer.getImageUrl());

                            Log.d("service", "insert data into table, " + entity.name);
                            AppDatabase.getDatabase(this).beerDao().insert(entity);
                        }


                        EventBus.getDefault().post(new BeersDownloadedEvent(list));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (intent.getAction().equals(ASYNC_ACTION)) {
                    // do a async call
                    List<Beer> results = new ArrayList<>();

                    BeerApiOkHttp.retrieveBeersAsync(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("service", "error getting beer lisr");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful() && response.body() != null) {
                                String respBody = response.body().string();
                                Gson gson = new GsonBuilder().create();
                                try {
                                    JSONArray jsonArray = new JSONArray(respBody);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String beerString = jsonArray.getString(i);
                                        Beer newBeer = gson.fromJson(beerString, Beer.class);
                                        results.add(newBeer);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            EventBus.getDefault().post(new BeersDownloadedEvent(results));
                        }
                    });
                } else {
                    // do retrofit get call

                    BeerApiRetrofitController controller = new BeerApiRetrofitController();
                    controller.connect();

                    controller.getListofBeers(new retrofit2.Callback<List<Beer>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<Beer>> call, retrofit2.Response<List<Beer>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                EventBus.getDefault().post(new BeersDownloadedEvent(response.body()));
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<Beer>> call, Throwable t) {
                            Log.e("service", "error getting list of beers");
                        }
                    });
                }
            }
        }
    }

    public static void startService(Context context, String action) {
        Intent intent = new Intent(context, GetBeersService.class);
        intent.setAction(action);

        context.startService(intent);
    }
}
