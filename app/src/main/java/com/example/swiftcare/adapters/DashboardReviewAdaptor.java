package com.example.swiftcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.R;
import com.example.swiftcare.activities.DonateDetailActivity;
import com.example.swiftcare.activities.FormReviewActivity;
import com.example.swiftcare.databinding.DashboardSingleViewBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class DashboardReviewAdaptor extends RecyclerView.Adapter<DashboardReviewAdaptor.DashboardAdminViewHolder>{
    private List<Donation> donationList;
    private Context context;

    public DashboardReviewAdaptor(Context context, List<Donation> donationList) {
        this.donationList = donationList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardReviewAdaptor.DashboardAdminViewHolder(
                DashboardSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdminViewHolder holder, int position) {
        Donation donation = donationList.get(position);

        ImageLoader.loadImage(donation.getDonationBanner(), holder.binding.donationBanner);
        holder.binding.donationTitle.setText(donation.getDonationTitle());
        holder.binding.fundraiserName.setText(donation.getFundraiserName());
        NumberFormat formatter = new DecimalFormat("#,###");
        String donationTarget = "Rp " + formatter.format(donation.getDonationTarget());
        holder.binding.donationTarget.setText(donationTarget);
        holder.binding.donationDuration.setText(donation.getDonationDuration());
        if(donation.status.equals(Constants.KEY_FORM_STATUS_ON_REVIEW)) {
            holder.binding.statusIcon.setImageResource(R.drawable.ic_eye);
            holder.binding.statusValue.setText("On Review");
            holder.binding.statusValue.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_600));
        } else if (donation.status.equals(Constants.KEY_FORM_STATUS_ACCEPTED)) {
            holder.binding.statusIcon.setImageResource(R.drawable.ic_verified);
            holder.binding.statusValue.setText("Accepted");
            holder.binding.statusValue.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_600));
        } else if (donation.status.equals(Constants.KEY_FORM_STATUS_REJECTED)) {
            holder.binding.statusIcon.setImageResource(R.drawable.ic_failed);
            holder.binding.statusValue.setText("Rejected");
            holder.binding.statusValue.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_D200));
        }

        holder.binding.statusIcon.setOnClickListener(v -> {
            if(donation.status.equals(Constants.KEY_FORM_STATUS_ON_REVIEW)) {
                Intent i = new Intent(context, FormReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_DONATION_ID, donation.donationId);
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    static class DashboardAdminViewHolder extends RecyclerView.ViewHolder {
        private final DashboardSingleViewBinding binding;

        DashboardAdminViewHolder(@NonNull DashboardSingleViewBinding dashboardSingleViewBinding) {
            super(dashboardSingleViewBinding.getRoot());
            binding = dashboardSingleViewBinding;
        }
    }
}
