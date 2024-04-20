package com.mihailap.olympiadfinder.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mihailap.olympiadfinder.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}