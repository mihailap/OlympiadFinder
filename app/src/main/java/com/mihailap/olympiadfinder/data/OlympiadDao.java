package com.mihailap.olympiadfinder.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

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

}
