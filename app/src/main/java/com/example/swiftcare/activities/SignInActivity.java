package com.example.swiftcare.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ActivitySignInBinding;
import com.example.swiftcare.fragments.CustomDialogFragment;
import com.example.swiftcare.fragments.HomeFragment;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.DialogUtils;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private TextView forgotPassword;
    private PreferenceManager preferenceManager;
    private GoogleSignInClient gClient;
    private GoogleSignInOptions gOptions;
    private String imageProfileBackground;
    private CustomDialogFragment customDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        customDialogFragment = new CustomDialogFragment();

        imageProfileBackground = "https://firebasestorage.googleapis.com/v0/b/swiftcare-86318.appspot.com/o/profileImages%2FdefaultImageProfileBackground.jpg?alt=media&token=b85f84aa-7f0e-4bbd-bc9d-50dd8673274a";

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(gAccount != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
        }

        binding.forgotPassword.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setListener();
    }

    private void setListener(){
        binding.buttonSignInGoogle.setOnClickListener( v ->
                activityResultLauncher.launch(gClient.getSignInIntent()));

        binding.buttonSignIn.setOnClickListener( v -> {
            if(isValidSignInDetail()) {
                signIn();
            }
        });

        binding.layoutSignUp.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.backButtonsignin.setOnClickListener(v ->
            startActivity(new Intent(getApplicationContext(), CTAActivity.class))
        );

        binding.forgotPassword.setOnClickListener(v -> {
            DialogUtils.showSimpleDialog(this, "Info", "Fitur ini masih dalam pengembangan. Mohon maaf atas ketidaknyamanannya.");
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            String email = account.getEmail();
                            String username = account.getDisplayName();
                            String imageProfile = account.getPhotoUrl().toString();

                            showLoadingDialog();
                            //Check if user exists
                            checkIfUserExists(email, username, imageProfile);

                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }, 2000);
                        }
                    } catch (ApiException e){
                        showToast("Something went wrong");
                    }
                }
            });

    private void signIn() {
        showLoadingDialog();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                        if (documentSnapshot.contains(Constants.KEY_IMAGE_PROFILE)) {
                            preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE));
                        }
                        if (documentSnapshot.contains(Constants.KEY_IMAGE_PROFILE_BACKGROUND)) {
                            preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE_BACKGROUND));
                        }
                        if (documentSnapshot.contains(Constants.KEY_PHONE_NUMBER)) {
                            preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        }
                        showFinishDialog(true, "Verification Successfully");
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }, 2000);
                    } else {
                        showFinishDialog(false, "Verification Failed");
                    }
                });
    }

    private void checkIfUserExists(String email, String username, String imageProfile) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("users");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, document.getId());
                            preferenceManager.putString(Constants.KEY_USERNAME, document.getString(Constants.KEY_USERNAME));
                            preferenceManager.putString(Constants.KEY_EMAIL, document.getString(Constants.KEY_EMAIL));
                            if (document.contains(Constants.KEY_IMAGE_PROFILE)) {
                                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, document.getString(Constants.KEY_IMAGE_PROFILE));
                            }
                            if (document.contains(Constants.KEY_IMAGE_PROFILE_BACKGROUND)) {
                                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, document.getString(Constants.KEY_IMAGE_PROFILE_BACKGROUND));
                            }
                            if (document.contains(Constants.KEY_PHONE_NUMBER)) {
                                preferenceManager.putString(Constants.KEY_PHONE_NUMBER, document.getString(Constants.KEY_PHONE_NUMBER));
                            }
                            showFinishDialog(true, "Verification Successfully");
                        } else {
                            saveUserToFirestore(email, username, imageProfile);
                        }
                    } else {
                        Exception exception = task.getException();
                        Log.e("Firestore", "Error checking user existence", exception);
                    }
                });
    }

    private void saveUserToFirestore(String email, String username, String imageProfile) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection(Constants.KEY_COLLECTION_USERS);

        Map<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_USERNAME, username);
        user.put(Constants.KEY_IMAGE_PROFILE, imageProfile);
        user.put(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackground);
        user.put(Constants.KEY_VERIFIED_STATUS, "Not Verified");

        usersRef.add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, username);
                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                    preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, imageProfile);
                    preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackground);
                    showFinishDialog(true, "Verification Successfully");
                })
                .addOnFailureListener(e -> {
                    showFinishDialog(false, "Verification Failed");
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetail() {
        if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
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
}