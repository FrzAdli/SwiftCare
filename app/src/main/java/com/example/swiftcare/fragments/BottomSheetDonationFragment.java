package com.example.swiftcare.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.BottomSheetDonationBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetDonationFragment extends BottomSheetDialogFragment {
    private BottomSheetDonationBinding binding;
    private List<MaterialCardView> cardViewList;
    public BottomSheetDonationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDonationBinding.inflate(getLayoutInflater());

        cardViewList = new ArrayList<>();
        cardViewList.add(binding.amount10);
        cardViewList.add(binding.amount20);
        cardViewList.add(binding.amount50);
        cardViewList.add(binding.amount70);
        cardViewList.add(binding.amount100);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {

        for (MaterialCardView cardView : cardViewList) {
            cardView.setOnClickListener(v -> {
                handleCardViewClick(cardView);
            });
        }

        binding.continueButton.setOnClickListener(v -> {
            dismiss();
            BottomSheetPaymentFragment bottomSheetPaymentFragment = new BottomSheetPaymentFragment();
            bottomSheetPaymentFragment.show(getParentFragmentManager(), bottomSheetPaymentFragment.getTag());
        });

    }

    private void handleCardViewClick(CardView clickedCardView) {
        for (MaterialCardView cardView : cardViewList) {
            int color = (cardView == clickedCardView) ? getResources().getColor(R.color.primary_400, getContext().getTheme()) :
                    getResources().getColor(R.color.white, getContext().getTheme());
            cardView.setStrokeColor(color);

            String bgAmountId = "bg_" + getResources().getResourceEntryName(cardView.getId());
            int bgAmountResId = getResources().getIdentifier(bgAmountId, "id", requireContext().getPackageName());
            MaterialCardView bgAmount = binding.getRoot().findViewById(bgAmountResId);

            int backgroundTint = (cardView.getId() == clickedCardView.getId()) ?
                    ContextCompat.getColor(requireContext(), R.color.primary_400) :
                    ContextCompat.getColor(requireContext(), R.color.primary_100);

            bgAmount.setBackgroundTintList(ColorStateList.valueOf(backgroundTint));

            if (cardView.getId() == clickedCardView.getId()) {
                String amountText = cardView.getTag().toString();
                binding.inputAmount.setText(amountText);
            }
        }
    }

}
