package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ActivityCtaactivityBinding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class CTAActivity extends AppCompatActivity {

    private ActivityCtaactivityBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCtaactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_ADMIN)) {
            Intent intent = new Intent(getApplicationContext(), DashboardAdminActivity.class);
            startActivity(intent);
            finish();
        } else if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(intent);
            finish();
        }


        imageSlider();
        setListener();
    }

    private void imageSlider() {
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slider1, null, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.slider2, null, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.slider3, null, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.slider4, null, ScaleTypes.CENTER_INSIDE));
        binding.imageSlider.setImageList(slideModels);
    }

    private void setListener(){
        binding.buttonSignIn.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
        binding.buttonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });
    }
}