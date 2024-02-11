package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ActivityFundraiserFormBinding;
import com.example.swiftcare.fragments.CustomDialogFragment;
import com.example.swiftcare.fragments.FundraiseFragment1;
import com.example.swiftcare.fragments.FundraiseFragment2;
import com.example.swiftcare.fragments.FundraiseFragment3;
import com.example.swiftcare.fragments.FundraiseFragment4;
import com.example.swiftcare.fragments.FundraiseFragment5;
import com.example.swiftcare.fragments.FundraiseFragment6;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.FormPreferenceManager;
import com.example.swiftcare.utilities.PreferenceManager;
import com.example.swiftcare.utilities.UriHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class FundraiserFormActivity extends AppCompatActivity {
    private ActivityFundraiserFormBinding binding;
    private HorizontalScrollView hsv1;
    private ImageView step1, step2, step3, step4, step5, step6;
    private PreferenceManager preferenceManager;
    private FormPreferenceManager formPreferenceManager;
    private FirebaseFirestore database;
    private CustomDialogFragment customDialogFragment;
    private int selectedForm = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFundraiserFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        formPreferenceManager = new FormPreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        customDialogFragment = new CustomDialogFragment();

        hsv1 = binding.hsv1;

        step1 = binding.step1;
        step2 = binding.step2;
        step3 = binding.step3;
        step4 = binding.step4;
        step5 = binding.step5;
        step6 = binding.step6;

        loadForm();
        setListeners();
    }

    private void loadForm() {
        //set form 1 by default
        binding.backStepButton.setVisibility(View.INVISIBLE);
        highlightStep(step1, 1);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, FundraiseFragment1.class, null)
                .commit();
    }

    private void setListeners() {
        binding.nextStepButton.setOnClickListener(v -> {
            switch (selectedForm) {
                case 1:
                    binding.backStepButton.setVisibility(View.VISIBLE);
                    highlightStep(step2, 2);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment2.class, null)
                            .commit();

                    selectedForm = 2;
                    break;
                case 2:
                    binding.backStepButton.setVisibility(View.VISIBLE);
                    highlightStep(step3, 3);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment3.class, null)
                            .commit();

                    selectedForm = 3;
                    break;
                case 3:
                    binding.backStepButton.setVisibility(View.VISIBLE);
                    highlightStep(step4, 4);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment4.class, null)
                            .commit();

                    selectedForm = 4;
                    break;
                case 4:
                    binding.backStepButton.setVisibility(View.VISIBLE);
                    highlightStep(step5, 5);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment5.class, null)
                            .commit();

                    selectedForm = 5;
                    break;
                case 5:
                    binding.backStepButton.setVisibility(View.VISIBLE);
                    binding.nextStepButton.setVisibility(View.GONE);
                    binding.submitButton.setVisibility(View.VISIBLE);
                    highlightStep(step6, 6);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment6.class, null)
                            .commit();

                    selectedForm = 6;
                    break;
                default:
                    break;
            }
        });

        binding.backStepButton.setOnClickListener(v -> {
            switch (selectedForm) {
                case 2:
                    binding.backStepButton.setVisibility(View.INVISIBLE);
                    highlightStep(step1, 1);
                    unhighlightStep(step2, 2);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment1.class, null)
                            .commit();

                    selectedForm = 1;
                    break;
                case 3:
                    highlightStep(step2, 2);
                    unhighlightStep(step3, 3);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment2.class, null)
                            .commit();

                    selectedForm = 2;
                    break;
                case 4:
                    highlightStep(step3, 3);
                    unhighlightStep(step4, 4);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment3.class, null)
                            .commit();

                    selectedForm = 3;
                    break;
                case 5:
                    highlightStep(step4, 4);
                    unhighlightStep(step5, 5);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment4.class, null)
                            .commit();

                    selectedForm = 4;
                    break;
                case 6:
                    binding.nextStepButton.setVisibility(View.VISIBLE);
                    binding.submitButton.setVisibility(View.GONE);
                    highlightStep(step5, 5);
                    unhighlightStep(step6, 6);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, FundraiseFragment5.class, null)
                            .commit();

                    selectedForm = 5;
                    break;
                default:
                    break;
            }
        });

        binding.submitButton.setOnClickListener(v -> {
            showLoadingDialog();
            HashMap<String, Object> formData = new HashMap<>();

            byte[] savedImage = formPreferenceManager.getByteArray(Constants.KEY_FORM_FUNDRAISE_IMAGE);
            Uri fileUri = UriHelper.getFileUri();
            if (savedImage != null && fileUri != null) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                String imageFileName = "image_" + System.currentTimeMillis() + ".jpg";
                StorageReference imageRef = storageRef.child("formImages/" + imageFileName);

                UploadTask uploadTask = imageRef.putBytes(savedImage);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        String pdfFileName = "file_" + System.currentTimeMillis() + ".pdf";
                        StorageReference pdfRef = storageRef.child("permissionLetter/" + pdfFileName);

                        UploadTask uploadPdfTask = pdfRef.putFile(fileUri);
                        uploadPdfTask.addOnSuccessListener(pdfTaskSnapshot -> {
                            pdfRef.getDownloadUrl().addOnSuccessListener(pdfUri -> {
                                String pdfUrl = pdfUri.toString();

                                //Fundraiser Data
                                formData.put(Constants.KEY_FUNDRAISER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                                formData.put(Constants.KEY_FORM_FUNDRAISER_NAME, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_NAME));
                                formData.put(Constants.KEY_FORM_FUNDRAISER_ID_NUMBER, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_ID_NUMBER));
                                formData.put(Constants.KEY_FORM_FUNDRAISER_PHONE_NUMBER, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_PHONE_NUMBER));
                                String birth = formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_BIRTH_PLACE) + ", " + formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_BIRTH_DATE);
                                formData.put(Constants.KEY_FORM_FUNDRAISER_BIRTH, birth);
                                formData.put(Constants.KEY_FORM_FUNDRAISER_EMAIL, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_EMAIL));
                                String address = formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_ADDRESS) + ", "
                                        + formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_SUBDISTRICT) + ", "
                                        + formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_CITY) + ", "
                                        + formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISER_PROVINCE);
                                formData.put(Constants.KEY_FORM_FUNDRAISER_ADDRESS, address);

                                //Recipient Data
                                formData.put(Constants.KEY_FORM_RECIPIENT_NAME, formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_NAME));
                                formData.put(Constants.KEY_FORM_RECIPIENT_PHONE_NUMBER, formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_PHONE_NUMBER));
                                formData.put(Constants.KEY_FORM_RECIPIENT_ADDRESS, formPreferenceManager.getString(Constants.KEY_FORM_RECIPIENT_ADDRESS));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_PURPOSE, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_PURPOSE));

                                //Fundraise Data
                                formData.put(Constants.KEY_FORM_FUNDRAISE_IMAGE, imageUrl);
                                formData.put(Constants.KEY_FORM_FUNDRAISE_TITLE, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_TITLE));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_VOLUNTEER, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_VOLUNTEER));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_NOMINAL, Long.parseLong(formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_NOMINAL)));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_DURATION, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DURATION));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_DATE_START, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DATE_START));
                                formData.put(Constants.KEY_FORM_FUNDRAISE_DATE_END, formPreferenceManager.getString(Constants.KEY_FORM_FUNDRAISE_DATE_END));

                                //Owner Data
                                formData.put(Constants.KEY_FORM_ACCOUNT_OWNER_NAME, formPreferenceManager.getString(Constants.KEY_FORM_ACCOUNT_OWNER_NAME));
                                formData.put(Constants.KEY_FORM_ACCOUNT_NUMBER, formPreferenceManager.getString(Constants.KEY_FORM_ACCOUNT_NUMBER));

                                //Permission Letter
                                formData.put(Constants.KEY_FORM_PERMISSION_LETTER, pdfUrl);

                                formData.put(Constants.KEY_FORM_STATUS, Constants.KEY_FORM_STATUS_ON_REVIEW);

                                database.collection(Constants.KEY_COLLECTION_FUNDRAISE_FORM).add(formData);
                                showFinishDialog(true, "We have received your form and we will review it soon");
                                formPreferenceManager.clear();
                                UriHelper.clearFileUri();

                                new Handler().postDelayed(() -> {
                                    Intent i = new Intent(getApplicationContext(), MainPageActivity.class);
                                    startActivity(i);
                                    finish();
                                }, 2000);

                            });
                        }).addOnFailureListener(exception -> {
                            showFinishDialog(false, "Failed to upload form");
                        });

                    });
                }).addOnFailureListener(exception -> {
                    showFinishDialog(false, "Failed to upload form");
                });
            }
        });
    }

    public void highlightStep(ImageView stepView, int stepNumber) {
        switch (stepNumber) {
            case 1:
                step1.setImageResource(R.drawable.step1);
                break;
            case 2:
                step2.setImageResource(R.drawable.step2);
                break;
            case 3:
                step3.setImageResource(R.drawable.step3);
                break;
            case 4:
                step4.setImageResource(R.drawable.step4);
                break;
            case 5:
                step5.setImageResource(R.drawable.step5);
                break;
            case 6:
                step6.setImageResource(R.drawable.step6);
                break;
            default:
                break;
        }

        hsv1.smoothScrollTo(stepView.getLeft(), 0);
    }

    public void unhighlightStep(ImageView stepView, int stepNumber) {
        switch (stepNumber) {
            case 1:
                step1.setImageResource(R.drawable.unstep1);
                break;
            case 2:
                step2.setImageResource(R.drawable.unstep2);
                break;
            case 3:
                step3.setImageResource(R.drawable.unstep3);
                break;
            case 4:
                step4.setImageResource(R.drawable.unstep4);
                break;
            case 5:
                step5.setImageResource(R.drawable.unstep5);
                break;
            case 6:
                step6.setImageResource(R.drawable.unstep6);
                break;
            default:
                break;
        }
    }

    private void showLoadingDialog() {
        customDialogFragment.show(getSupportFragmentManager(), CustomDialogFragment.class.getSimpleName());
    }

    private void showFinishDialog(Boolean status, String text) {
        customDialogFragment.binding.progressBar.setVisibility(View.INVISIBLE);

        if(status) {
            customDialogFragment.binding.messageTextView.setText(text);
            customDialogFragment.binding.successImage.setVisibility(View.VISIBLE);
            customDialogFragment.binding.failedImage.setVisibility(View.INVISIBLE);
        } else {
            customDialogFragment.binding.messageTextView.setText(text);
            customDialogFragment.binding.successImage.setVisibility(View.INVISIBLE);
            customDialogFragment.binding.failedImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        formPreferenceManager.clear();
    }
}