package com.example.swiftcare.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.swiftcare.R;
import com.example.swiftcare.activities.DonateDetailActivity;
import com.example.swiftcare.adapters.DonateAdapter;
import com.example.swiftcare.databinding.FragmentDonateBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateFragment extends Fragment {

    private  FragmentDonateBinding binding;
    private List<Donation> donationList;
    private DonateAdapter donateAdapter;
    private FirebaseFirestore database;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DonateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonateFragment newInstance(String param1, String param2) {
        DonateFragment fragment = new DonateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDonateBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        loadDonationData();
        setListeners();
    }

    private void setListeners(){
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.dataNotFound.setVisibility(View.INVISIBLE);
                binding.donateRecyclerView.setVisibility(View.VISIBLE);

                loadDonationData();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void init() {
        donationList = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        binding.donateRecyclerView.setHasFixedSize(true);
        binding.donateRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void loadDonationData() {
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

                if (donationList.isEmpty()) {
                    binding.dataNotFound.setVisibility(View.VISIBLE);
                    binding.donateRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    binding.dataNotFound.setVisibility(View.INVISIBLE);
                    binding.donateRecyclerView.setVisibility(View.VISIBLE);
                    donateAdapter = new DonateAdapter(requireContext(), donationList);
                    binding.donateRecyclerView.setAdapter(donateAdapter);
                }
            } else {
                binding.dataNotFound.setVisibility(View.VISIBLE);
                binding.donateRecyclerView.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void filterList(String text){
        List<Donation> filteredList = new ArrayList<>();
        for (Donation donation : donationList){
            if(donation.getDonationTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(donation);
            }
        }

        if(filteredList.isEmpty()) {
            binding.dataNotFound.setVisibility(View.VISIBLE);
            binding.donateRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            binding.dataNotFound.setVisibility(View.INVISIBLE);
            binding.donateRecyclerView.setVisibility(View.VISIBLE);
            donateAdapter.setFilteredList(filteredList);
        }
    }
}