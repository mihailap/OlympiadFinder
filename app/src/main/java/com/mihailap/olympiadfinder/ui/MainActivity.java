package com.mihailap.olympiadfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        setProgressBar();
        setSupportActionBar(binding.toolbar);
        setFilter();
        setUpCheckBoxListeners();
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

    private void setUpCheckBoxListeners() {
        RadioGroup radioGroup = binding.checkboxGroup;

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view = radioGroup.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // if checkbox selected, filter
                        filter(binding.searchBar.getQuery().toString());
                    }
                });
            }
        }
    }


    // Filter for searchbar
    private void filter(String text) {
        if (tempList != null) {

            RadioGroup radioGroup = binding.checkboxGroup;
            StringBuilder selectedTextsBuilder = new StringBuilder();

            List<Integer> selectedGrades = new ArrayList<>();

            int childCount = radioGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = radioGroup.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        String selectedText = checkBox.getText().toString();
                        selectedTextsBuilder.append(selectedText).append(" ");
                        int grade = Integer.parseInt(selectedText);
                        selectedGrades.add(grade);
                    }
                }
            }
            // Transform "8 10" to "8 9 10" etc
            if (!selectedGrades.isEmpty()) {
                int minGrade = Collections.min(selectedGrades);
                int maxGrade = Collections.max(selectedGrades);
                for (int i = minGrade + 1; i < maxGrade; i++) {
                    selectedTextsBuilder.append(i).append(" ");
                }
            }

            ArrayList<Olympiad> filteredList = new ArrayList<>();
            for (Olympiad item : tempList) {
                if ((item.getKeywords().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase()))
                        && containsAllGrades(item.getGradesList(), selectedGrades)) {
                    filteredList.add(item);
                }
            }

            if (filteredList.isEmpty()) {
                binding.tvNothingFound.setVisibility(View.VISIBLE);
            } else {
                binding.tvNothingFound.setVisibility(View.GONE);
            }

            olympiadAdapter.setOlympiadList(filteredList);
        }
    }

    // Does grades contains in olympiad
    private boolean containsAllGrades(String gradesList, List<Integer> selectedGrades) {
        // Splitting grades string to array
        String[] gradesArray = gradesList.split(" ");
        for (int grade : selectedGrades) {
            String gradeString = String.valueOf(grade);
            boolean found = false;
            for (String word : gradesArray) {
                if (word.equals(gradeString)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }


    private void setFilter() {
        binding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle filters when clicked
                if (binding.filtersSelect.getVisibility() == View.VISIBLE) {
                    binding.filtersSelect.setVisibility(View.GONE);
                } else {
                    binding.filtersSelect.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setProgressBar() {
        viewModel.getMaxProgressLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer maxProgress) {
                binding.progressBar.setMax(maxProgress);
            }
        });
        viewModel.getCurrentProgressLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.progressBar.setProgress(integer, true);
                if (Objects.equals(viewModel.getMaxProgressLiveData().getValue(), integer)) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvDataLoading.setVisibility(View.GONE);
                    binding.customSearch.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
