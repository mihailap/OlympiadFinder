package com.mihailap.olympiadfinder.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.data.OlympiadDao;
import com.mihailap.olympiadfinder.data.OlympiadDatabase;
import com.mihailap.olympiadfinder.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private OlympiadDatabase olympiadDB;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: FIX MULTIPLE SAME ELEMENTS
        // # MAGIC CODE #
        RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        olympiadDB = Room.databaseBuilder(getApplicationContext(), OlympiadDatabase.class, "OlympiadDB")
                .addCallback(myCallback).build();



        // TESTING DB
        Olympiad NTO = new Olympiad("NTO", "Phys", "8-11", false, 3, "example.com");
        Olympiad VSOSH = new Olympiad("VSOSH", "Every", "4-11", false, 1, "example.org");
        addOlympiadInBackground(NTO);
        addOlympiadInBackground(VSOSH);

    }


    public void addOlympiadInBackground(Olympiad olympiad) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                // olympiadDB.clearAllTables();  // FIXME!
                olympiadDB.getOlympiadDao().addOlympiad(olympiad);
            }
        });
    }
}