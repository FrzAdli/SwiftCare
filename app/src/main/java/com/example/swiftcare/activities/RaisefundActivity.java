package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swiftcare.databinding.ActivityRaisefundBinding;

public class RaisefundActivity extends AppCompatActivity {

    ActivityRaisefundBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRaisefundBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}