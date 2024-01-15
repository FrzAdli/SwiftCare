package com.example.swiftcare.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.activities.DonateDetailActivity;
import com.example.swiftcare.databinding.DonateSingleViewBinding;
import com.example.swiftcare.databinding.FragmentDonateBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DonateAdapter extends RecyclerView.Adapter<DonateAdapter.DonateViewHolder> {

    private List<Donation> donationList;
    private Context context;

    public void setFilteredList(List<Donation> filteredListList) {
        this.donationList = filteredListList;
        notifyDataSetChanged();
    }

    public DonateAdapter(Context context, List<Donation> donationList) {
        this.context = context;
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public DonateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonateViewHolder(
                DonateSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DonateViewHolder holder, int position) {
        Donation donation = donationList.get(position);

        holder.binding.donationTitle.setText(donation.getDonationTitle());
        holder.binding.fundraiserName.setText(donation.getFundraiserName());
        NumberFormat formatter = new DecimalFormat("#,###");
        String donationTarget = "Rp " + formatter.format(donation.getDonationTarget());
        holder.binding.donationTarget.setText(donationTarget);

        //Converting Date to days left
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar todayCalendar = Calendar.getInstance();
        Date today = todayCalendar.getTime();
        long diffInMillies = donation.getDonationEnd().getTime() - today.getTime();
        long daysleft = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        holder.binding.donationDays.setText((int) daysleft + " days left");

        holder.binding.layoutDonation.setOnClickListener(v -> {
            Intent i = new Intent(context, DonateDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_DONATION_ID, donation.donationId);
            bundle.putString(Constants.KEY_FUNDRAISER_ID, donation.fundraiserId);
            bundle.putString(Constants.KEY_FUNDRAISER_NAME, donation.fundraiserName);
            bundle.putString(Constants.KEY_DONATION_TITLE, donation.donationTitle);
            bundle.putString(Constants.KEY_DONATION_DESC, donation.donationDesc);
            bundle.putString(Constants.KEY_DONATION_TARGET, donationTarget);
            bundle.putLong(Constants.KEY_DONATION_START, donation.donationStart.getTime());
            bundle.putLong(Constants.KEY_DONATION_END, donation.donationEnd.getTime());
            i.putExtras(bundle);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    static class DonateViewHolder extends RecyclerView.ViewHolder {
        private final DonateSingleViewBinding binding;

        DonateViewHolder(@NonNull DonateSingleViewBinding donateSingleViewBinding) {
            super(donateSingleViewBinding.getRoot());
            binding = donateSingleViewBinding;
        }
    }
}