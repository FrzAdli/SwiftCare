package com.example.swiftcare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.swiftcare.R;
import com.example.swiftcare.activities.CommunityActivity;
import com.example.swiftcare.activities.MessageActivity;
import com.example.swiftcare.activities.RaisefundActivity;
import com.example.swiftcare.activities.SwiftbotActivity;
import com.example.swiftcare.adapters.DonateAdapter;
import com.example.swiftcare.adapters.UrgentfundAdapter;
import com.example.swiftcare.databinding.FragmentHomeBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.DialogUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private List<Donation> donationList;
    private FirebaseFirestore database;
    private UrgentfundAdapter urgentfundAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        imageSlider();
        loadDonationData();
        setListeners();

    }

    private void init() {
        donationList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        binding.urgentFundRecyclerView.setHasFixedSize(true);
        binding.urgentFundRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setListeners() {
        binding.RaiseFund.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RaisefundActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });

        binding.SwiftbotAI.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SwiftbotActivity.class));
        });

        binding.DonationSchedule.setOnClickListener(v -> {
            DialogUtils.showSimpleDialog(requireContext(), "Information", "This feature is still under development. Sorry for the inconvenience.");
        });

        binding.Community.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CommunityActivity.class));
        });

        binding.iconchat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MessageActivity.class));
        });
    }

    private void loadDonationData() {
        CollectionReference donationRef = database.collection(Constants.KEY_COLLECTION_DONATIONS);

        donationRef.addSnapshotListener((value, error) -> {
            if (!isAdded()) {
                return;
            }
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

                    System.out.println(document.getString(Constants.KEY_DONATION_TITLE));

                    donationList.add(donation);
                }

                if (donationList.isEmpty()) {
                    binding.dataNotFound.setVisibility(View.VISIBLE);
                    binding.urgentFundRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    binding.dataNotFound.setVisibility(View.INVISIBLE);
                    binding.urgentFundRecyclerView.setVisibility(View.VISIBLE);
                    urgentfundAdapter = new UrgentfundAdapter(requireContext(), donationList);
                    binding.urgentFundRecyclerView.setAdapter(urgentfundAdapter);
                }
            } else {
                binding.dataNotFound.setVisibility(View.VISIBLE);
                binding.urgentFundRecyclerView.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void imageSlider() {
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.sliderhome1, null, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.sliderhome1, null, ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.sliderhome1, null, ScaleTypes.CENTER_INSIDE));
        binding.imageSlider.setImageList(slideModels);

    }

}