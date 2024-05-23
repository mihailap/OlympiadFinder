package com.mihailap.olympiadfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.mihailap.olympiadfinder.R;
import com.mihailap.olympiadfinder.data.Olympiad;
import com.mihailap.olympiadfinder.databinding.ActivityFavouriteOlympiadBinding;

import java.util.ArrayList;
import java.util.List;


public class FavouriteOlympiadActivity extends AppCompatActivity {
    private ActivityFavouriteOlympiadBinding binding;
    private FavouriteOlympiadViewModel viewModel;
    private OlympiadAdapter olympiadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteOlympiadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(FavouriteOlympiadViewModel.class);

        buildRecycler();
        viewModel.refreshList();
        setSupportActionBar(binding.toolbar);
        setFilters();
        setUpCheckBoxListeners();

    }

    private void buildRecycler() {
        olympiadAdapter = new OlympiadAdapter();
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        olympiadAdapter.setOnOlympiadClickListener(new OlympiadAdapter.OnOlympiadClickListener() {
            @Override
            public void onOlympiadClick(Olympiad olympiad) {
                Intent intent = OlympiadInfoActivity.newIntent(FavouriteOlympiadActivity.this, olympiad);
                startActivity(intent);
            }
        });
        viewModel.getOlympiads().observe(this, new Observer<List<Olympiad>>() {
            @Override
            public void onChanged(List<Olympiad> olympiads) {
                olympiadAdapter.setOlympiadList(olympiads);
                if (olympiads.isEmpty()) {
                    binding.tvNothingFound.setText("В избранном ничего нет...");
                    binding.tvNothingFound.setVisibility(View.VISIBLE);
                } else {
                    binding.tvNothingFound.setText(R.string.nothing_found);
                    binding.tvNothingFound.setVisibility(View.GONE);
                }
            }
        });
        binding.recyclerView.setAdapter(olympiadAdapter);
    }

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
                viewModel.filter(newText, getSelectedGrades());
                // when filtered we need to update screen
                viewModel.getFilteredOlympiads().observe(FavouriteOlympiadActivity.this, new Observer<List<Olympiad>>() {
                    @Override
                    public void onChanged(List<Olympiad> olympiads) {
                        if (olympiads.isEmpty()) {
                            binding.tvNothingFound.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvNothingFound.setVisibility(View.GONE);
                        }
                        olympiadAdapter.setOlympiadList(olympiads);
                    }
                });
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
                        viewModel.filter(binding.searchBar.getQuery().toString(), getSelectedGrades());
                        // when filtered we need to update screen
                        viewModel.getFilteredOlympiads().observe(FavouriteOlympiadActivity.this, new Observer<List<Olympiad>>() {
                            @Override
                            public void onChanged(List<Olympiad> olympiads) {
                                if (olympiads.isEmpty()) {
                                    binding.tvNothingFound.setVisibility(View.VISIBLE);
                                } else {
                                    binding.tvNothingFound.setVisibility(View.GONE);
                                }
                                olympiadAdapter.setOlympiadList(olympiads);
                            }
                        });
                    }
                });
            }
        }
    }

    private List<Integer> getSelectedGrades() {
        List<Integer> selectedGrades = new ArrayList<>();
        for (int i = 0; i < binding.checkboxGroup.getChildCount(); i++) {
            View view = binding.checkboxGroup.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    String selectedText = checkBox.getText().toString();
                    int grade = Integer.parseInt(selectedText);
                    selectedGrades.add(grade);
                }
            }
        }
        return selectedGrades;
    }

    private void setFilters() {
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


    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.refreshList();
    }
}