package com.example.beerapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.beerapp.database.AppDatabase;
import com.example.beerapp.database.BeerEntity;
import com.example.beerapp.database.BeerViewModel;
import com.example.beerapp.database.BeerViewModelFactory;
import com.example.beerapp.networking.Beer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView beerList;
    private BeerAdapter adapter;
    private List<Beer> beers = new ArrayList<>();
    private MySharedPref prefFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populateDummyBeers();

        beerList = findViewById(R.id.beerList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.scrollToPosition(0);


        adapter = new BeerAdapter(beers);
        beerList.setLayoutManager(layoutManager);
        beerList.setAdapter(adapter);


        prefFile = new MySharedPref(this);
        prefFile.addStringToSharedPref("beer", "Ciucas");


        GetBeersService.startService(this, GetBeersService.SYNC_ACTION);

        //GetBeersService.startService(this, GetBeersService.ASYNC_ACTION);

       //GetBeersService.startService(this, GetBeersService.RETROFIT_GET_ACTION);


        // get the viewModel using custom Factory
        BeerViewModel viewModel = ViewModelProviders.of(this, new BeerViewModelFactory(getApplication())).get(BeerViewModel.class);

        // add observe to liveData manually updated by developer with postValues
        viewModel.getBeerList().observe(this, new Observer<List<Beer>>() {
            @Override
            public void onChanged(@Nullable List<Beer> beers) {
                Log.d("mainActivity", "onChanged - list of beers");

                if (beers != null) {
                    adapter.updateData(beers);
                }
            }
        });

        // when room update entries to BeerEntity table
        viewModel.getLiveBeerList().observe(this, new Observer<List<BeerEntity>>() {
            @Override
            public void onChanged(@Nullable List<BeerEntity> beerEntities) {
                Log.d("mainActivity", "### onChanged - list of beers ###");

                beers.clear();
                if (beerEntities != null) {
                    for(int i=0;i<beerEntities.size();i++) {
                        BeerEntity entity = beerEntities.get(i);
                        Beer beer = new Beer(Integer.parseInt(entity.id), entity.name, entity.desc, entity.imageUrl);
                        beers.add(beer);
                    }

                    adapter.updateData(beers);
                }
            }
        });


        BeerEntity entity = AppDatabase.getDatabase(this).beerDao().getBeerById(1);
        AppDatabase.getDatabase(this).beerDao().delete(entity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BeersDownloadedEvent event) {
        Log.d("mainActivity", "eventbus message received!");

        adapter.updateData(event.getBeers());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void populateDummyBeers() {
        beers.add(new Beer(1, "Name 1", "Desc 1", ""));
        beers.add(new Beer(1, "Name 2", "Desc 2", ""));
        beers.add(new Beer(1, "Name 3", "Desc 3", ""));
        beers.add(new Beer(1, "Name 4", "Desc 4", ""));
        beers.add(new Beer(1, "Name 5", "Desc 5", ""));
        beers.add(new Beer(1, "Name 6", "Desc 6", ""));
        beers.add(new Beer(1, "Name 7", "Desc 7", ""));
        beers.add(new Beer(1, "Name 8", "Desc 8", ""));
        beers.add(new Beer(1, "Name 9", "Desc 9", ""));
        beers.add(new Beer(1, "Name 10", "Desc 10", ""));
        beers.add(new Beer(1, "Name 11", "Desc 11", ""));
        beers.add(new Beer(1, "Name 12", "Desc 12", ""));
        beers.add(new Beer(1, "Name 13", "Desc 13", ""));
        beers.add(new Beer(1, "Name 14", "Desc 14", ""));
    }
}
