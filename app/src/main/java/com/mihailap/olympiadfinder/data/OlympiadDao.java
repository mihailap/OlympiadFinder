package com.mihailap.olympiadfinder.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface OlympiadDao {

    @Query("SELECT isFavourite FROM olympiad WHERE id = :id")
    boolean getFavouriteStatus(int id);
    @Query("SELECT * FROM olympiad ORDER BY id")
    Single<List<Olympiad>> getOlympiads();

    @Query("SELECT * FROM olympiad WHERE isFavourite = 1 ORDER BY id")
    Single<List<Olympiad>> getFavouriteOlympiads();

    @Query("UPDATE Olympiad SET isFavourite = :isFavorite WHERE id = :id")
    Completable updateFavouriteStatus(int id, Boolean isFavorite);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOlympiad(Olympiad olympiad);

    @Query("DELETE FROM olympiad WHERE id =:id")
    Completable remove(int id);

    @Query("DELETE FROM olympiad")
    Completable removeAll();
}
