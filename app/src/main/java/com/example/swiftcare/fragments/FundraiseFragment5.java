package com.example.swiftcare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.FragmentFundraise5Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;

public class FundraiseFragment5 extends Fragment {
    private FragmentFundraise5Binding binding;
    private FormPreferenceManager formPreferenceManager;

    public FundraiseFragment5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise5Binding.inflate(getLayoutInflater());
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
        binding.inputAccountOwnerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_ACCOUNT_OWNER_NAME, s.toString());
            }
        });

        binding.inputAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_ACCOUNT_NUMBER, s.toString());
            }
        });
    }

    private void loadSavedData() {
        binding.inputAccountOwnerName.setText(formPreferenceManager.getString(Constants.KEY_FORM_ACCOUNT_OWNER_NAME));
        binding.inputAccountNumber.setText(formPreferenceManager.getString(Constants.KEY_FORM_ACCOUNT_NUMBER));
    }
}