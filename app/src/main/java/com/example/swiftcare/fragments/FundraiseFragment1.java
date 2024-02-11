package com.example.swiftcare.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.example.swiftcare.databinding.FragmentFundraise1Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class FundraiseFragment1 extends Fragment {
    private FragmentFundraise1Binding binding;
    private FormPreferenceManager formPreferenceManager;

    public FundraiseFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise1Binding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        formPreferenceManager = new FormPreferenceManager(requireContext());

        setListeners();
        loadSavedData();

    }

    private void setListeners() {
        binding.inputFundraiserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_NAME, s.toString());
            }
        });

        binding.inputFundraiserIdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_ID_NUMBER, s.toString());
            }
        });

        binding.inputFundraiserPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_PHONE_NUMBER, s.toString());
            }
        });

        binding.inputFundraiserBirthPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_BIRTH_PLACE, s.toString());
            }
        });

        binding.inputFundraiserBirthDate.setOnClickListener(v -> {
            showBirthDatePicker();
        });

        binding.inputFundraiserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_EMAIL, s.toString());
            }
        });

        binding.inputFundraiserProvince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_PROVINCE, s.toString());
            }
        });

        binding.inputFundraiserCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_CITY, s.toString());
            }
        });

        binding.inputFundraiserSubdistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_SUBDISTRICT, s.toString());
            }
        });

        binding.inputFundraiserAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_ADDRESS, s.toString());
            }
        });

    }

    private void loadSavedData() {
        binding.inputFundraiserName.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_NAME));
        binding.inputFundraiserIdNumber.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_ID_NUMBER));
        binding.inputFundraiserPhoneNumber.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_PHONE_NUMBER));
        binding.inputFundraiserBirthPlace.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_BIRTH_PLACE));
        binding.inputFundraiserBirthDate.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_BIRTH_DATE));
        binding.inputFundraiserEmail.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_EMAIL));
        binding.inputFundraiserProvince.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_PROVINCE));
        binding.inputFundraiserCity.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_CITY));
        binding.inputFundraiserSubdistrict.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_SUBDISTRICT));
        binding.inputFundraiserAddress.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_ADDRESS));
    }


    private void showBirthDatePicker() {
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
                        binding.inputFundraiserBirthDate.setText(selectedDate);
                        formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISER_BIRTH_DATE, selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

}