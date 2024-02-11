package com.example.swiftcare.fragments;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.FragmentFundraise4Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FundraiseFragment4 extends Fragment {
    private FragmentFundraise4Binding binding;
    private FormPreferenceManager formPreferenceManager;
    private List<MaterialButton> materialButtonList;

    public FundraiseFragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise4Binding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formPreferenceManager = new FormPreferenceManager(requireContext());

        materialButtonList = new ArrayList<>();
        materialButtonList.add(binding.oneMonth);
        materialButtonList.add(binding.threeMonth);
        materialButtonList.add(binding.sixMonth);
        materialButtonList.add(binding.oneYear);

        setListeners();
        loadSavedData();
    }

    private void setListeners() {
        for (MaterialButton materialButton : materialButtonList) {
            materialButton.setOnClickListener(v -> {
                handleMaterialButton(materialButton);
                saveDuration(materialButton.getText().toString());
            });
        }

        binding.inputNominal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_NOMINAL, s.toString());
            }
        });

        binding.inputDateStart.setOnClickListener(v -> {
            showDateStartPicker();
        });

        binding.inputDateFInish.setOnClickListener(v -> {
            showDateFinishPicker();
        });
    }

    private void saveDuration(String duration) {
        formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_DURATION, duration);
    }

    private void loadSavedData() {
        binding.inputNominal.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_NOMINAL));
        String savedDateStart = formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DATE_START);
        if (savedDateStart != null) {
            binding.inputDateStart.setText(savedDateStart);
        }
        String savedDateEnd = formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DATE_END);
        if (savedDateEnd != null) {
            binding.inputDateFInish.setText(savedDateEnd);
        }

        String savedDuration = formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DURATION);
        if (savedDuration != null) {
            for (MaterialButton materialButton : materialButtonList) {
                if (materialButton.getText().toString().equals(savedDuration)) {
                    handleMaterialButton(materialButton);
                    break;
                }
            }
        }
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
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        binding.inputDateStart.setText(selectedDate);
                        formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_DATE_START, selectedDate);
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
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                        binding.inputDateFInish.setText(selectedDate);
                        formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_DATE_END, selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}