package com.example.swiftcare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.swiftcare.databinding.ActivityCtaactivityBinding;

public class CTAActivity extends AppCompatActivity {

    ActivityCtaactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCtaactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}