package com.example.swiftcare.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.FragmentDescriptionBinding;
import com.example.swiftcare.utilities.Constants;

public class DescriptionFragment extends Fragment {

    private FragmentDescriptionBinding binding;
    private String description;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString(Constants.KEY_DONATION_DESC);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.donationDesc.setText(description);
        binding.donationDesc.setMaxLines(5);
        Drawable arrowDown = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down);
        Drawable arrowUp = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down);

        binding.ivArrow.setOnClickListener(v -> {
            if (binding.donationDesc.getMaxLines() == 5) {
                binding.donationDesc.setMaxLines(Integer.MAX_VALUE);
                binding.ivArrow.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowUp, null);
                binding.gradientWhite.setVisibility(View.INVISIBLE);
            } else {
                binding.donationDesc.setMaxLines(5);
                binding.ivArrow.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowDown, null);
                binding.gradientWhite.setVisibility(View.VISIBLE);
            }
        });
    }

}