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
import com.example.swiftcare.databinding.FragmentFundraise2Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;

public class FundraiseFragment2 extends Fragment {
    private FragmentFundraise2Binding binding;
    private FormPreferenceManager formPreferenceManager;

    public FundraiseFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise2Binding.inflate(getLayoutInflater());
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
        binding.inputRecipientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_RECIPIENT_NAME, s.toString());
            }
        });

        binding.inputRecipientPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_RECIPIENT_PHONE_NUMBER, s.toString());
            }
        });

        binding.inputRecipientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_RECIPIENT_ADDRESS, s.toString());
            }
        });

        binding.inputFundraisingPurpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_PURPOSE, s.toString());
            }
        });
    }

    private void loadSavedData() {
        binding.inputRecipientName.setText(formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_NAME));
        binding.inputRecipientPhoneNumber.setText(formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_PHONE_NUMBER));
        binding.inputRecipientAddress.setText(formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_ADDRESS));
        binding.inputFundraisingPurpose.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_PURPOSE));
    }
}