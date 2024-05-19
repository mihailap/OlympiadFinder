package com.mihailap.olympiadfinder.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Olympiad.class}, version = 2)
public abstract class OlympiadDatabase extends RoomDatabase {
    private static final String DB_NAME = "OlympiadDB3.db";
    private static OlympiadDatabase instance = null;

    public static OlympiadDatabase newInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application, OlympiadDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }

    public abstract OlympiadDao olympiadDao();


}
