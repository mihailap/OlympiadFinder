package com.mihailap.olympiadfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mihailap.olympiadfinder.R;
import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.data.OlympiadDatabase;
import com.mihailap.olympiadfinder.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private OlympiadAdapter olympiadAdapter;
    private List<Olympiad> tempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        buildDB();
        buildRecycler();

        viewModel.parseOlympiads();
        setSupportActionBar(binding.toolbar);
    }

    private void buildDB() {
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

        OlympiadDatabase olympiadDB = Room.databaseBuilder(getApplicationContext(), OlympiadDatabase.class, "OlympiadDB")
                .addCallback(myCallback).build();
    }

    private void buildRecycler() {
        olympiadAdapter = new OlympiadAdapter();
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        olympiadAdapter.setOnOlympiadClickListener(new OlympiadAdapter.OnOlympiadClickListener() {
            @Override
            public void onOlympiadClick(Olympiad olympiad) {
                Intent intent = OlympiadInfoActivity.newIntent(MainActivity.this, olympiad);
                startActivity(intent);
            }
        });
        viewModel.getOlympiads().observe(this, new Observer<List<Olympiad>>() {
            @Override
            public void onChanged(List<Olympiad> olympiads) {
                olympiadAdapter.setOlympiadList(olympiads);
                tempList = olympiads;
            }
        });
        binding.recyclerView.setAdapter(olympiadAdapter);
    }

    // SearchBar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    // Filter for searchbar
    private void filter(String text) {
        ArrayList<Olympiad> filteredList = new ArrayList<>();
        for (Olympiad item : tempList) {
            if (item.getKeywords().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Nothing Found...", Toast.LENGTH_SHORT).show();
        }
        olympiadAdapter.setOlympiadList(filteredList);
    }
}