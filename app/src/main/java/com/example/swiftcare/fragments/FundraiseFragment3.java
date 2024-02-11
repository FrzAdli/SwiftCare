package com.example.swiftcare.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.FragmentFundraise1Binding;
import com.example.swiftcare.databinding.FragmentFundraise3Binding;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FundraiseFragment3 extends Fragment {
    private FragmentFundraise3Binding binding;
    private FormPreferenceManager formPreferenceManager;


    public FundraiseFragment3() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFundraise3Binding.inflate(inflater, container, false);
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
        binding.uploadImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(i);
        });

        binding.inputTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_TITLE, editable.toString());
            }
        });

        binding.inputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION, editable.toString());
            }
        });

        binding.inputVolunteer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                formPreferenceManager.putString(Constants.KEY_FORM_FUNDRAISE_VOLUNTEER, editable.toString());
            }
        });

    }

    private void loadSavedData() {
        byte[] savedImage = formPreferenceManager.getByteArray(Constants.KEY_FORM_FUNDRAISE_IMAGE);
        if (savedImage != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(savedImage, 0, savedImage.length);
            binding.uploadImage.setImageBitmap(bitmap);
        }
        binding.inputTitle.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_TITLE));
        binding.inputDescription.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION));
        binding.inputVolunteer.setText(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_VOLUNTEER));
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            Bitmap processedBitmap = processImage(bitmap);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            processedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            formPreferenceManager.putByteArray(Constants.KEY_FORM_FUNDRAISE_IMAGE, byteArray);

                            binding.uploadImage.setImageBitmap(processedBitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Bitmap processImage(Bitmap originalBitmap) {
        float targetAspectRatio = 16f / 9f;

        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        float currentAspectRatio = (float) width / height;

        if (currentAspectRatio > targetAspectRatio) {
            int newWidth = (int) (height * targetAspectRatio);
            int startX = (width - newWidth) / 2;
            return Bitmap.createBitmap(originalBitmap, startX, 0, newWidth, height);
        } else if (currentAspectRatio < targetAspectRatio) {
            int newHeight = (int) (width / targetAspectRatio);
            int startY = (height - newHeight) / 2;
            return Bitmap.createBitmap(originalBitmap, 0, startY, width, newHeight);
        } else {
            return originalBitmap;
        }
    }

}