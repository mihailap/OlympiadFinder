package com.mihailap.olympiadfinder.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface OlympiadDao {
    @Insert
    void addOlympiad(Olympiad olympiad);

    @Update
    void updateOlympiad(Olympiad olympiad);

    @Delete
    void deleteOlympiad(Olympiad olympiad);

    @Query("select * from olympiad")
    List<Olympiad> getAllOlympiad();

    @Query("select * from olympiad where id==:id")
    Olympiad getOlympiad(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable add(Olympiad olympiad);
}
