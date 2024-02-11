package com.example.swiftcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;

import com.example.swiftcare.R;
import com.example.swiftcare.adapters.DashboardActiveAdapter;
import com.example.swiftcare.adapters.DashboardReviewAdaptor;
import com.example.swiftcare.adapters.DonateAdapter;
import com.example.swiftcare.databinding.ActivityDashboardAdminBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdminActivity extends AppCompatActivity {
    private ActivityDashboardAdminBinding binding;
    private List<Donation> donationList;
    private DashboardReviewAdaptor dashboardReviewAdaptor;
    private DashboardActiveAdapter dashboardActiveAdapter;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        loadReviewData();
        setListeners();
    }

    private void init() {
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(getApplicationContext(), gOptions);

        donationList = new ArrayList<>();
        preferenceManager = new PreferenceManager(getApplicationContext());

        database = FirebaseFirestore.getInstance();

        binding.dashboardRecyclerView.setHasFixedSize(true);
        binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void loadReviewData() {
        CollectionReference formsRef = database.collection(Constants.KEY_COLLECTION_FUNDRAISE_FORM);

        formsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", "Error getting data", error);
                return;
            }

            donationList.clear();

            if (value != null && !value.isEmpty()) {
                for (DocumentSnapshot document : value.getDocuments()) {
                    Donation donation = new Donation();
                    donation.donationId = document.getId();
                    donation.fundraiserName = document.getString(Constants.KEY_FORM_FUNDRAISER_NAME);
                    donation.donationTitle = document.getString(Constants.KEY_FORM_FUNDRAISE_TITLE);
                    donation.donationTarget = document.getLong(Constants.KEY_FORM_FUNDRAISE_NOMINAL);
                    donation.donationBanner = document.getString(Constants.KEY_FORM_FUNDRAISE_IMAGE);
                    donation.donationDuration = document.getString(Constants.KEY_FORM_FUNDRAISE_DURATION);
                    donation.status = document.getString(Constants.KEY_FORM_STATUS);

                    donationList.add(donation);
                }

                dashboardReviewAdaptor = new DashboardReviewAdaptor(getApplicationContext(), donationList);
                binding.dashboardRecyclerView.setAdapter(dashboardReviewAdaptor);
            }
        });
    }

    private void loadActiveData() {
        CollectionReference donationRef = database.collection(Constants.KEY_COLLECTION_DONATIONS);

        donationRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", "Error getting donation list", error);
                return;
            }

            donationList.clear();

            if (value != null && !value.isEmpty()) {
                for (DocumentSnapshot document : value.getDocuments()) {
                    Donation donation = new Donation();
                    donation.donationId = document.getId();
                    donation.fundraiserId = document.getString(Constants.KEY_FUNDRAISER_ID);
                    donation.fundraiserName = document.getString(Constants.KEY_FUNDRAISER_NAME);
                    donation.donationTitle = document.getString(Constants.KEY_DONATION_TITLE);
                    donation.donationDesc = document.getString(Constants.KEY_DONATION_DESC);
                    Timestamp donationStartTimestamp = document.getTimestamp(Constants.KEY_DONATION_START);
                    donation.donationStart = donationStartTimestamp != null ? donationStartTimestamp.toDate() : null;
                    Timestamp donationEndTimestamp = document.getTimestamp(Constants.KEY_DONATION_END);
                    donation.donationEnd = donationEndTimestamp != null ? donationEndTimestamp.toDate() : null;
                    donation.donationTarget = document.getLong(Constants.KEY_DONATION_TARGET);
                    //Photo
                    donation.donationBanner = document.getString(Constants.KEY_DONATION_BANNER);
                    if (document.contains(Constants.KEY_IMAGE_URL1)) {
                        donation.imageUrl1 =  document.getString(Constants.KEY_IMAGE_URL1);
                    }
                    if (document.contains(Constants.KEY_IMAGE_URL2)) {
                        donation.imageUrl2 =  document.getString(Constants.KEY_IMAGE_URL2);
                    }

                    donationList.add(donation);
                }

                dashboardActiveAdapter = new DashboardActiveAdapter(getApplicationContext(), donationList);
                binding.dashboardRecyclerView.setAdapter(dashboardActiveAdapter);
            }

        });
    }

    private void setListeners() {
        binding.icLogout.setOnClickListener(v -> {
            gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    preferenceManager.clear();
                    Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });

        binding.onReviewFilter.setOnClickListener(v -> {
            binding.onReviewFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N500)));
            binding.activeFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.onReviewFilter.setIconResource(R.drawable.bg_circle);
            binding.onReviewFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.primary_600)));
            binding.activeFilter.setIconResource(R.drawable.ic_circle);
            binding.activeFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setIconResource(R.drawable.ic_circle);
            binding.expiredFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.onReviewFilter.setBackgroundResource(R.drawable.bg_round_50_active);
            binding.onReviewFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_50)));
            binding.activeFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.activeFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.expiredFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            loadReviewData();
        });

        binding.activeFilter.setOnClickListener(v -> {
            binding.activeFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N500)));
            binding.onReviewFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.activeFilter.setIconResource(R.drawable.bg_circle);
            binding.activeFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.primary_600)));
            binding.onReviewFilter.setIconResource(R.drawable.ic_circle);
            binding.onReviewFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setIconResource(R.drawable.ic_circle);
            binding.expiredFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.activeFilter.setBackgroundResource(R.drawable.bg_round_50_active);
            binding.activeFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_50)));
            binding.onReviewFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.onReviewFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.expiredFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.expiredFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            loadActiveData();
        });

        binding.expiredFilter.setOnClickListener(v -> {
            binding.expiredFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N500)));
            binding.activeFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.onReviewFilter.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.expiredFilter.setIconResource(R.drawable.bg_circle);
            binding.expiredFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.primary_600)));
            binding.activeFilter.setIconResource(R.drawable.ic_circle);
            binding.activeFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.onReviewFilter.setIconResource(R.drawable.ic_circle);
            binding.onReviewFilter.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));

            binding.expiredFilter.setBackgroundResource(R.drawable.bg_round_50_active);
            binding.expiredFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_50)));
            binding.activeFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.activeFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
            binding.onReviewFilter.setBackgroundResource(R.drawable.bg_stroke_rounded);
            binding.onReviewFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.neutral_N50)));
        });
    }
}