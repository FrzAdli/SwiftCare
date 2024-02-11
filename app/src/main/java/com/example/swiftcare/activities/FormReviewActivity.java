package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.example.swiftcare.databinding.ActivityFormReviewBinding;
import com.example.swiftcare.models.Donation;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.ImageLoader;
import com.example.swiftcare.utilities.MD5Hash;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class FormReviewActivity extends AppCompatActivity {
    private ActivityFormReviewBinding binding;
    private String formId, permissionLetterUrl;
    private String fundraiserId, fundraiserName, donationBanner, donationTitle, donationDesc, imageUrl1;
    private Date donationStart, donationEnd;
    private Long donationTarget;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        formId = getIntent().getExtras().getString(Constants.KEY_DONATION_ID);
        database = FirebaseFirestore.getInstance();

        loadData();
        setListeners();
    }

    private void loadData() {
        CollectionReference donationRef = database.collection(Constants.KEY_COLLECTION_FUNDRAISE_FORM);

        donationRef.document(formId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        fundraiserId = documentSnapshot.getString(Constants.KEY_FUNDRAISER_ID);
                        fundraiserName = documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_NAME);
                        donationBanner = documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_IMAGE);
                        donationTitle = documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_TITLE);
                        donationDesc = documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION);
                        imageUrl1 = documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_IMAGE);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            Date dateStart = dateFormat.parse(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DATE_START));
                            TimeZone timeZoneStart = TimeZone.getTimeZone("UTC+7");
                            Calendar calendarStart = Calendar.getInstance();
                            calendarStart.setTime(dateStart);
                            calendarStart.setTimeZone(timeZoneStart);
                            donationStart = calendarStart.getTime();

                            Date dateEnd = dateFormat.parse(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DATE_END));
                            TimeZone timeZoneEnd = TimeZone.getTimeZone("UTC+7");
                            Calendar calendarEnd = Calendar.getInstance();
                            calendarEnd.setTime(dateStart);
                            calendarEnd.setTimeZone(timeZoneEnd);
                            donationEnd = calendarEnd.getTime();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        donationTarget = documentSnapshot.getLong(Constants.KEY_FORM_FUNDRAISE_NOMINAL);

                        binding.FundraiserName.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_NAME));
                        binding.FundraiserIdNumber.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_ID_NUMBER));
                        binding.FundraiserPhoneNumber.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_PHONE_NUMBER));
                        binding.FundraiserBirth.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_BIRTH));
                        binding.FundraiserEmail.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_EMAIL));
                        binding.FundraiserAddress.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISER_ADDRESS));

                        binding.RecipientName.setText(documentSnapshot.getString(Constants.KEY_FORM_RECIPIENT_NAME));
                        binding.RecipientPhoneNumber.setText(documentSnapshot.getString(Constants.KEY_FORM_RECIPIENT_PHONE_NUMBER));
                        binding.RecipientAddress.setText(documentSnapshot.getString(Constants.KEY_FORM_RECIPIENT_ADDRESS));
                        binding.FundraisingPurpose.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_PURPOSE));

                        ImageLoader.loadImage(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_IMAGE), binding.FundraiseImage);
                        binding.FundraiseTitle.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_TITLE));
                        binding.FundraiseDescription.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DESCRIPTION));
                        binding.FundraiseVolunteer.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_VOLUNTEER));

                        NumberFormat formatter = new DecimalFormat("#,###");
                        String donationNominal = "Rp " + formatter.format(documentSnapshot.getLong(Constants.KEY_FORM_FUNDRAISE_NOMINAL));
                        binding.FundraiseNominal.setText(donationNominal);
                        binding.FundraiseDuration.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DURATION));
                        binding.FundraiseDateStart.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DATE_START));
                        binding.FundraiseDateEnd.setText(documentSnapshot.getString(Constants.KEY_FORM_FUNDRAISE_DATE_END));

                        binding.AccountOwnerName.setText(documentSnapshot.getString(Constants.KEY_FORM_ACCOUNT_OWNER_NAME));
                        binding.AccountNumber.setText(documentSnapshot.getString(Constants.KEY_FORM_ACCOUNT_NUMBER));

                        permissionLetterUrl = documentSnapshot.getString(Constants.KEY_FORM_PERMISSION_LETTER);

                    } else {
                        Log.d("FormReviewActivity", "Document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FormReviewActivity", "Error getting document", e);
                });
    }

    private void setListeners() {
        binding.backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.PermissionLetter.setOnClickListener(v -> {
            if (permissionLetterUrl != null) {
                downloadFileFromUrl(permissionLetterUrl);
            }
        });

        binding.acceptButton.setOnClickListener(v -> {
            updateFormStatus(Constants.KEY_FORM_STATUS_ACCEPTED);
        });

        binding.rejectButton.setOnClickListener(v -> {
            updateFormStatus(Constants.KEY_FORM_STATUS_REJECTED);
        });
    }

    private void updateFormStatus(String newStatus) {
        loading(true);
        CollectionReference donationRef = database.collection(Constants.KEY_COLLECTION_FUNDRAISE_FORM);

        donationRef.document(formId)
                .update(Constants.KEY_FORM_STATUS, newStatus)
                .addOnSuccessListener(aVoid -> {
                    if(newStatus.equals(Constants.KEY_FORM_STATUS_ACCEPTED)) {
                        addDonation();
                    }
                    getOnBackPressedDispatcher().onBackPressed();
                    loading(false);
                })
                .addOnFailureListener(e -> {
                    loading(false
                    );
                });
    }

    private void addDonation() {
        HashMap<String, Object> donationData = new HashMap<>();
        donationData.put(Constants.KEY_FUNDRAISER_ID, fundraiserId);
        donationData.put(Constants.KEY_FUNDRAISER_NAME, fundraiserName);
        donationData.put(Constants.KEY_DONATION_TITLE, donationTitle);
        donationData.put(Constants.KEY_DONATION_DESC, donationDesc);
        donationData.put(Constants.KEY_DONATION_BANNER, donationBanner);
        donationData.put(Constants.KEY_IMAGE_URL1, imageUrl1);
        donationData.put(Constants.KEY_DONATION_START, donationStart);
        donationData.put(Constants.KEY_DONATION_END, donationEnd);
        donationData.put(Constants.KEY_DONATION_TARGET, donationTarget);
        donationData.put(Constants.KEY_DONATION_STATUS, Constants.KEY_DONATION_STATUS_ACTIVE);
        database.collection(Constants.KEY_COLLECTION_DONATIONS)
                .add(donationData)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                });
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBackground.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.progressBackground.setVisibility(View.GONE);
        }
    }

    private void downloadFileFromUrl(String fileUrl) {
        loading(true);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setTitle("Download Permission Letter");
        request.setDescription("Downloading...");

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "permission_letter.pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        long downloadId = downloadManager.enqueue(request);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long receivedDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (receivedDownloadId == downloadId) {
                    openDownloadedFile(downloadId);
                }
            }
        }, filter);
    }


    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId != -1) {
                openDownloadedFile(downloadId);
            }
        }
    };

    protected void onDestroy() {
        if (onComplete != null) {
            unregisterReceiver(onComplete);
        }

        super.onDestroy();
    }

    private void openDownloadedFile(long downloadId) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                int fileUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String downloadedUriString = cursor.getString(fileUriIndex);

                Uri contentUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        new File(Uri.parse(downloadedUriString).getPath())
                );

                loading(false);
                openFile(contentUri);
            }
        }

        cursor.close();
    }


    private void openFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

}