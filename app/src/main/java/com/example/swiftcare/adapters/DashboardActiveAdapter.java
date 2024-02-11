package com.example.swiftcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.databinding.DonateSingleViewBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.ImageLoader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DashboardActiveAdapter extends RecyclerView.Adapter<DashboardActiveAdapter.DashboardActiveViewHolder> {
    private List<Donation> donationList;
    private Context context;

    public DashboardActiveAdapter(Context context, List<Donation> donationList) {
        this.context = context;
        this.donationList = donationList;
    }

    @NonNull
    @Override
    public DashboardActiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardActiveAdapter.DashboardActiveViewHolder(
                DonateSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardActiveViewHolder holder, int position) {
        Donation donation = donationList.get(position);

        ImageLoader.loadImage(donation.getDonationBanner(), holder.binding.donationBanner);
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
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    static class DashboardActiveViewHolder extends RecyclerView.ViewHolder {
        private final DonateSingleViewBinding binding;

        DashboardActiveViewHolder(@NonNull DonateSingleViewBinding donateSingleViewBinding) {
            super(donateSingleViewBinding.getRoot());
            binding = donateSingleViewBinding;
        }
    }
}
