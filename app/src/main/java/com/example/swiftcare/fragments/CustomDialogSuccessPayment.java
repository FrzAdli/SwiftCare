package com.example.swiftcare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.swiftcare.activities.MainPageActivity;
import com.example.swiftcare.databinding.CustomDialogLayoutBinding;
import com.example.swiftcare.databinding.CustomDialogSuccessPaymentBinding;

public class CustomDialogSuccessPayment extends DialogFragment {

    public CustomDialogSuccessPaymentBinding binding;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = CustomDialogSuccessPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();

        binding.messageTextView1.setVisibility(View.GONE);
        binding.messageTextView2.setText("Loading...");
        binding.successImage.setVisibility(View.INVISIBLE);
        binding.buttonHome.setVisibility(View.GONE);

        binding.buttonHome.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), MainPageActivity.class);
            startActivity(i);
            dismiss();
        });

        return dialog;
    }

    public void setSuccessCondition() {
        new Handler().postDelayed(() -> {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.messageTextView1.setVisibility(View.VISIBLE);
            binding.messageTextView2.setText("Thank you");
            binding.successImage.setVisibility(View.VISIBLE);
            binding.buttonHome.setVisibility(View.VISIBLE);
        }, 3000);
    }
}
