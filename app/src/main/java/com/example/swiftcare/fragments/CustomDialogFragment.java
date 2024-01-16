package com.example.swiftcare.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.CustomDialogLayoutBinding;

public class CustomDialogFragment extends DialogFragment {

    public CustomDialogLayoutBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = CustomDialogLayoutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();

        binding.messageTextView.setText("Loading...");

        return dialog;
    }
}
