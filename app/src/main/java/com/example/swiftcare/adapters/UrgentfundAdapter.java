package com.example.swiftcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.activities.DonateDetailActivity;
import com.example.swiftcare.databinding.DonateSingleViewBinding;
import com.example.swiftcare.databinding.UrgentfundSingleViewBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UrgentfundAdapter extends RecyclerView.Adapter<UrgentfundAdapter.UrgentfundViewHolder> {
    private List<Donation> donationList;
    private Context context;

    public UrgentfundAdapter(Context context, List<Donation> donationList) {
        this.donationList = donationList;
        this.context = context;
    }

    @NonNull
    @Override
    public UrgentfundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UrgentfundViewHolder(
                UrgentfundSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UrgentfundViewHolder holder, int position) {
        Donation donation = donationList.get(position);

        ImageLoader.loadImage(donation.getDonationBanner(), holder.binding.donationImages);
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

        holder.binding.layoutUrgentfund.setOnClickListener(v -> {
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
            if(donation.getImageUrl1() != null){
                bundle.putString(Constants.KEY_IMAGE_URL1, donation.getImageUrl1());
            }
            if(donation.getImageUrl2() != null){
                bundle.putString(Constants.KEY_IMAGE_URL2, donation.getImageUrl2());
            }
            i.putExtras(bundle);
            context.startActivity(i);
        });
    }


    @Override
    public int getItemCount() {
        return donationList.size();
    }

    static class UrgentfundViewHolder extends RecyclerView.ViewHolder {
        private final UrgentfundSingleViewBinding binding;

        UrgentfundViewHolder(@NonNull UrgentfundSingleViewBinding urgentfundSingleViewBinding) {
            super(urgentfundSingleViewBinding.getRoot());
            binding = urgentfundSingleViewBinding;
        }
    }
}
