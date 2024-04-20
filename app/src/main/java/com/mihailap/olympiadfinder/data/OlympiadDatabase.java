package com.mihailap.olympiadfinder.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Olympiad.class}, version = 1)
public abstract class OlympiadDatabase extends RoomDatabase {
    public abstract OlympiadDao getOlympiadDao();
}
