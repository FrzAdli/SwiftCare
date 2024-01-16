package com.example.swiftcare.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swiftcare.R;
import com.example.swiftcare.activities.SignInActivity;
import com.example.swiftcare.databinding.FragmentProfileBinding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    private PreferenceManager preferenceManager;
    private String verifiedStatus;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(requireContext(), gOptions);
        preferenceManager = new PreferenceManager(requireContext());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadVerifiedStatus();
        loadProfile();
        binding.logoutButton.setOnClickListener( v -> {
            gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    preferenceManager.clear();
                    Intent i = new Intent(requireActivity(), SignInActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });
    }

    private void loadProfile() {
        ImageLoader.loadCircleImage(preferenceManager.getString(Constants.KEY_IMAGE_PROFILE), binding.userProfile);
        binding.profileName.setText(preferenceManager.getString(Constants.KEY_USERNAME));
    }

    @SuppressLint("ResourceAsColor")
    private void loadVerifiedStatus() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null) {
            DocumentReference userDocument = database.collection("users").document(userId);
            userDocument.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String verifiedStatus = documentSnapshot.getString(Constants.KEY_VERIFIED_STATUS);
                    if (verifiedStatus != null) {
                        binding.verified.setText(verifiedStatus);
                        int colorResource = getResources().getColor(R.color.primary_800);
                        ColorStateList colorStateList = ColorStateList.valueOf(colorResource);
                        binding.verified.setBackgroundTintList(colorStateList);
                        binding.badgeImageView.setImageResource(R.drawable.verified_badge);
                    }
                } else {
                    Log.d("Firestore", "Dokumen tidak ditemukan");
                }
            }).addOnFailureListener(e -> {
                Log.e("Firestore", "Gagal mengambil data", e);
            });
        }
    }

}