package com.example.swiftcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swiftcare.databinding.FragmentFundraise6Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;
import com.example.swiftcare.utilities.UriHelper;

public class FundraiseFragment6 extends Fragment {
    private FragmentFundraise6Binding binding;
    private FormPreferenceManager formPreferenceManager;

    public FundraiseFragment6() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise6Binding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        formPreferenceManager = new FormPreferenceManager(requireContext());

        setListeners();
        loadSavedData();
    }

    private void setListeners() {
        binding.inputFundraisingPermissionLetter.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            pickFileLauncher.launch(intent);
        });

        binding.icUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            pickFileLauncher.launch(intent);
        });
    }

    private void loadSavedData() {
        Uri fileUri = UriHelper.getFileUri();
        if (fileUri != null) {
            String fileName = getFileName(fileUri);
            binding.inputFundraisingPermissionLetter.setText(fileName);
        }
    }

    private final ActivityResultLauncher<Intent> pickFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        UriHelper.setFileUri(fileUri);
                        String fileName = getFileName(fileUri);
                        binding.inputFundraisingPermissionLetter.setText(fileName);
                    }
                }
            }
    );

    private String getFileName(Uri uri) {
        String result = null;
        Cursor cursor = null;

        try {
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                result = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }

}