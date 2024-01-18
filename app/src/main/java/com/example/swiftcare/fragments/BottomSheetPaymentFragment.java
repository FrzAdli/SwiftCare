package com.example.swiftcare.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.BottomSheetPaymentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetPaymentFragment extends BottomSheetDialogFragment {
    private BottomSheetPaymentBinding binding;
    private List<MaterialCardView> cardViewList;

    public BottomSheetPaymentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetPaymentBinding.inflate(getLayoutInflater());

        cardViewList = new ArrayList<>();
        cardViewList.add(binding.gopay);
        cardViewList.add(binding.dana);
        cardViewList.add(binding.ovo);
        cardViewList.add(binding.spay);
        cardViewList.add(binding.bankBCA);
        cardViewList.add(binding.bankBNI);
        cardViewList.add(binding.bankBRI);
        cardViewList.add(binding.bankMANDIRI);
        cardViewList.add(binding.bankBSI);
        cardViewList.add(binding.bankPERMATA);
        cardViewList.add(binding.bankCIMB);
        cardViewList.add(binding.bankSEA);

        setListener();

        return binding.getRoot();
    }

    private void setListener() {
        for (MaterialCardView cardView : cardViewList) {
            cardView.setOnClickListener(v -> {
                handleCardViewClick(cardView);
            });
        }

        binding.backButton.setOnClickListener(v -> {
            dismiss();
            BottomSheetDonationFragment bottomSheetDonationFragment = new BottomSheetDonationFragment();
            bottomSheetDonationFragment.show(getParentFragmentManager(), bottomSheetDonationFragment.getTag());
        });

        binding.paymentButton.setOnClickListener(v -> {
            CustomDialogSuccessPayment successDialog = new CustomDialogSuccessPayment();
            successDialog.show(getChildFragmentManager(), "SuccessDialog");

            successDialog.setSuccessCondition();
        });
    }

    private void handleCardViewClick(CardView clickedCardView) {
        for (MaterialCardView cardView : cardViewList) {
            int color = (cardView == clickedCardView) ? getResources().getColor(R.color.primary_600, getContext().getTheme()) :
                    getResources().getColor(R.color.white, getContext().getTheme());
            cardView.setStrokeColor(color);
        }
    }
}

