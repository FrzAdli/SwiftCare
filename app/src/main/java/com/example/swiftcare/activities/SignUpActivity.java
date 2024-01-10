package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.swiftcare.activities.CTAActivity;
import com.example.swiftcare.activities.SignInActivity;
import com.example.swiftcare.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();

    }

    private void setListener(){
        binding.layoutSignIn.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
        binding.backButtonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CTAActivity.class));
        });
    }
}