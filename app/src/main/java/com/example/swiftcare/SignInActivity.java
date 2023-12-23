package com.example.swiftcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.swiftcare.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.forgotPassword.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setListener();
    }

    private void setListener(){
        binding.layoutSignUp.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.backButtonsignin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CTAActivity.class));
        });
    }
}