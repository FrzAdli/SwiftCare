package com.example.swiftcare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.swiftcare.R;
import com.example.swiftcare.adapters.DonateAdapter;
import com.example.swiftcare.adapters.LovelistAdapter;
import com.example.swiftcare.databinding.FragmentDonateBinding;
import com.example.swiftcare.databinding.FragmentLovelistBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LovelistFragment extends Fragment {
    private FragmentLovelistBinding binding;
    private List<Donation> donationList;
    private LovelistAdapter lovelistAdapter;
    private FirebaseFirestore database;
    private FirebaseDatabase realtimeDatabase;
    private DatabaseReference rootRef;
    private PreferenceManager preferenceManager;

    public LovelistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLovelistBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.searchContainer.clearFocus();
        binding.searchContainer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        init();
        loadLikedDonations();
        setListeners();
    }

    private void setListeners() {
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.noFavorite.setVisibility(View.INVISIBLE);
                binding.lovelistRecyclerView.setVisibility(View.VISIBLE);

                loadLikedDonations();
                binding.refreshLayout.setRefreshing(false);
            }
        });

    }

    private void init() {
        donationList = new ArrayList<>();
        preferenceManager = new PreferenceManager(requireContext());

        database = FirebaseFirestore.getInstance();
        realtimeDatabase = FirebaseDatabase.getInstance();
        rootRef = realtimeDatabase.getReference();

        binding.lovelistRecyclerView.setHasFixedSize(true);
        binding.lovelistRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void loadLikedDonations() {
        loading(true);
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null) {
            DatabaseReference userLikesRef = rootRef.child("loveList");

            userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> likedDonationIds = new ArrayList<>();

                    for (DataSnapshot donationSnapshot : dataSnapshot.getChildren()) {
                        String donationId = donationSnapshot.getKey();
                        if (donationSnapshot.child(userId).exists()) {
                            likedDonationIds.add(donationId);
                        }
                    }

                    if (!likedDonationIds.isEmpty()) {
                        getDonationData(likedDonationIds);
                    } else {
                        loading(false);
                        showNoLikedDonationsMessage();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loading(false);
                    showNoLikedDonationsMessage();
                }
            });
        }
    }


    private void getDonationData(List<String> likedDonationIds) {
        CollectionReference donationRef = database.collection(Constants.KEY_COLLECTION_DONATIONS);

        donationRef.whereIn(FieldPath.documentId(), likedDonationIds)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        donationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
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

                        updateUIWithLikedDonations();
                    } else {
                        Log.e("Firestore", "Error getting liked donations", task.getException());
                        loading(false);
                    }
                });
    }

    private void updateUIWithLikedDonations() {
        if (isAdded() && getContext() != null) {
            if (donationList != null && !donationList.isEmpty()) {
                loading(false);
                binding.noFavorite.setVisibility(View.INVISIBLE);
                binding.lovelistRecyclerView.setVisibility(View.VISIBLE);
                lovelistAdapter = new LovelistAdapter(requireContext(), donationList);
                binding.lovelistRecyclerView.setAdapter(lovelistAdapter);
            } else {
                loading(false);
                showNoLikedDonationsMessage();
            }
        }
    }

    private void showNoLikedDonationsMessage() {
        binding.noFavorite.setVisibility(View.VISIBLE);
        binding.lovelistRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.noFavorite.setVisibility(View.INVISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.noFavorite.setVisibility(View.VISIBLE);
        }
    }

    private void filterList(String text){
        List<Donation> filteredList = new ArrayList<>();
        for (Donation donation : donationList){
            if(donation.getDonationTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(donation);
            }
        }

        if(filteredList.isEmpty()) {
            binding.noFavorite.setVisibility(View.VISIBLE);
            binding.lovelistRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            binding.noFavorite.setVisibility(View.INVISIBLE);
            binding.lovelistRecyclerView.setVisibility(View.VISIBLE);
            lovelistAdapter.setFilteredList(filteredList);
        }
    }
}