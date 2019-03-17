package com.example.beerapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BeerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BeerEntity beerEntity);

    @Delete
    void delete(BeerEntity beerEntity);

    @Query("SELECT * FROM beerentity")
    List<BeerEntity> getAll();

    @Query("SELECT * FROM beerentity WHERE id IN (:beerId)")
    BeerEntity getBeerById(int beerId);

    @Query("SELECT * FROM beerentity")
    LiveData<List<BeerEntity>> getLiveDataList();

}
