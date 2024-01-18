package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.DatePicker;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ActivityFundraiserForm2Binding;
import com.example.swiftcare.fragments.CustomDialogSuccessFundraise;
import com.example.swiftcare.fragments.CustomDialogSuccessPayment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FundraiserForm2 extends AppCompatActivity {
    private ActivityFundraiserForm2Binding binding;
    private List<MaterialButton> materialButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFundraiserForm2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        materialButtonList = new ArrayList<>();
        materialButtonList.add(binding.oneMonth);
        materialButtonList.add(binding.threeMonth);
        materialButtonList.add(binding.sixMonth);
        materialButtonList.add(binding.oneYear);

        setListeners();
    }

    private void setListeners() {
        for (MaterialButton materialButton : materialButtonList) {
            materialButton.setOnClickListener(v -> {
                handleMaterialButton(materialButton);
            });
        }

        binding.inputDateStart.setOnClickListener(v -> {
            showDateStartPicker();
        });

        binding.inputDateFInish.setOnClickListener(v -> {
            showDateFinishPicker();
        });

        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.submitButton.setOnClickListener(v -> {
            CustomDialogSuccessFundraise successDialog = new CustomDialogSuccessFundraise();
            successDialog.show(getSupportFragmentManager(), "SuccessDialog");

            successDialog.setSuccessCondition();
        });

        binding.cancelButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

    }

    private void handleMaterialButton(MaterialButton clickedMaterialButton) {
        for (MaterialButton materialButton : materialButtonList) {
            materialButton.setIconTintResource(R.color.neutral_N500);
            materialButton.setBackgroundResource(R.drawable.bg_stroke_rounded);
            materialButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            materialButton.setStrokeWidth(0);
            materialButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N300)));

            if (materialButton == clickedMaterialButton) {
                materialButton.setIconTintResource(R.color.primary_600);
                materialButton.setBackgroundResource(R.drawable.bg_round_50);
                materialButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.primary_800)));
                materialButton.setStrokeWidth(5);
                materialButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_100)));
            }
        }
    }

    private void showDateStartPicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        binding.inputDateStart.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showDateFinishPicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        binding.inputDateFInish.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}