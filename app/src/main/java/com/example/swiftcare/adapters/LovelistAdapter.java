package com.example.swiftcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftcare.activities.DonateDetailActivity;
import com.example.swiftcare.databinding.DonateSingleViewBinding;
import com.example.swiftcare.databinding.LovelistSingleViewBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LovelistAdapter extends RecyclerView.Adapter<LovelistAdapter.LovelistViewHolder> {
    private List<Donation> donationList;
    private Context context;
    private PreferenceManager preferenceManager;
    private FirebaseDatabase realtimeDatabase;
    private DatabaseReference rootRef;

    public void setFilteredList(List<Donation> filteredListList) {
        this.donationList = filteredListList;
        notifyDataSetChanged();
    }

    public LovelistAdapter(Context context, List<Donation> donationList) {
        this.donationList = donationList;
        this.context = context;
    }

    @NonNull
    @Override
    public LovelistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LovelistAdapter.LovelistViewHolder(
                LovelistSingleViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LovelistViewHolder holder, int position) {
        preferenceManager = new PreferenceManager(context);
        realtimeDatabase = FirebaseDatabase.getInstance();
        rootRef = realtimeDatabase.getReference();

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

        holder.binding.deleteIcon.setOnClickListener(v -> {
            removeFromFavorites(donation.getDonationId());
        });

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

    static class LovelistViewHolder extends RecyclerView.ViewHolder {
        private final LovelistSingleViewBinding binding;

        LovelistViewHolder(@NonNull LovelistSingleViewBinding lovelistSingleViewBinding) {
            super(lovelistSingleViewBinding.getRoot());
            binding = lovelistSingleViewBinding;
        }
    }

    private void removeFromFavorites(String donationId) {
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null) {
            DatabaseReference loveListRef = rootRef.child("loveList").child(donationId);

            loveListRef.child(userId).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int removedPosition = removeItem(donationId);
                            notifyItemRemoved(removedPosition);
                            notifyItemRangeChanged(removedPosition, getItemCount());
                        } else {
                            Log.e("Firebase Realtime DB", "Error removing item from favorites", task.getException());
                        }
                    });
        }
    }

    private int removeItem(String donationId) {
        int position = -1;

        for (int i = 0; i < donationList.size(); i++) {
            Donation donation = donationList.get(i);

            if (donation.getDonationId().equals(donationId)) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            donationList.remove(position);
        }

        return position;
    }
}
