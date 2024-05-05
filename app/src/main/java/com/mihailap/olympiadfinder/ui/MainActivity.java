package com.mihailap.olympiadfinder.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.data.OlympiadDatabase;
import com.mihailap.olympiadfinder.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OlympiadDatabase olympiadDB;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private OlympiadAdapter olympiadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

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

        // Adding elements TODO: remake this somehow
        // i think this causes memory leaks!

        Olympiad NTO = new Olympiad(1, "NTO", "Phys", "8-11", false, 3, "example.com");
        Olympiad VSOSH = new Olympiad(2, "VSOSH", "Every", "4-11", false, 1, "example.org");
        Olympiad aaa = new Olympiad(3, "Test", "Yes", "4", false, 1, "example.org");
        viewModel.saveOlympiad(NTO);
        viewModel.saveOlympiad(VSOSH);
        viewModel.saveOlympiad(aaa);
        Log.d("MainTEST", "LOADED");

        // RECYCLER VIEW
        olympiadAdapter = new OlympiadAdapter();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        viewModel.getOlympiads().observe(this, new Observer<List<Olympiad>>() {
            @Override
            public void onChanged(List<Olympiad> olympiads) {
                olympiadAdapter.setOlympiadList(olympiads);
            }
        });

        binding.recyclerView.setAdapter(olympiadAdapter);
        viewModel.refreshList();
    }
}