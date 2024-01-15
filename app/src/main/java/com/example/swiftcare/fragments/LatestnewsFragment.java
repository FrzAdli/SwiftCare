package com.example.swiftcare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.FragmentDescriptionBinding;
import com.example.swiftcare.databinding.FragmentLatestnewsBinding;
import com.example.swiftcare.utilities.Constants;

public class LatestnewsFragment extends Fragment {

    private FragmentLatestnewsBinding binding;
    private String title1;

    public LatestnewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title1 = getArguments().getString("title1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLatestnewsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.title1.setText(title1);
    }
}