package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.swiftcare.R;
import com.example.swiftcare.adapters.DonateDetailAdapter;
import com.example.swiftcare.databinding.ActivityDonateDetailBinding;
import com.example.swiftcare.fragments.BottomSheetDonationFragment;
import com.example.swiftcare.fragments.DescriptionFragment;
import com.example.swiftcare.fragments.LatestnewsFragment;
import com.example.swiftcare.fragments.VolunteerFragment;
import com.example.swiftcare.utilities.Constants;
import com.example.swiftcare.utilities.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DonateDetailActivity extends AppCompatActivity {

    private ActivityDonateDetailBinding binding;
    private DonateDetailAdapter donateDetailAdapter;
    private DescriptionFragment descriptionFragment;
    private LatestnewsFragment latestnewsFragment;
    private VolunteerFragment volunteerFragment;
    private FirebaseDatabase database;
    private DatabaseReference rootRef;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();

        imageSlider();
        loadDetail();
        setFragment();
        setListeners();
    }

    private void setListeners() {
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.donateLayout.setOnClickListener(v -> {
            BottomSheetDonationFragment bottomSheetDonationFragment = new BottomSheetDonationFragment();
            bottomSheetDonationFragment.show(getSupportFragmentManager(), bottomSheetDonationFragment.getTag());
        });

        binding.icLove.setOnClickListener(v -> {
            lovePost();
        });

        binding.chatLayout.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TARGET_ID, getIntent().getExtras().getString(Constants.KEY_FUNDRAISER_ID));
            i.putExtras(bundle);
            startActivity(i);
        });
    }

    private void loadDetail() {
        binding.donationTitle.setText(getIntent().getExtras().getString(Constants.KEY_DONATION_TITLE));
        binding.fundTarget.setText(getIntent().getExtras().getString(Constants.KEY_DONATION_TARGET));

        //Setting Countdown Timer
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar todayCalendar = Calendar.getInstance();
        Date today = todayCalendar.getTime();
        long diffInMillies = getIntent().getExtras().getLong(Constants.KEY_DONATION_END) - today.getTime();
        long daysleft = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long secondsLeft = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        setCountdown(secondsLeft);
        binding.timeLeft.setText(daysleft + " days left");

        binding.fundTarget.setText(getIntent().getExtras().getString(Constants.KEY_DONATION_TARGET));
        binding.fundraiserName.setText(getIntent().getExtras().getString(Constants.KEY_FUNDRAISER_NAME));

        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        DatabaseReference loveListRef = rootRef.child("loveList").child(getIntent().getExtras().getString(Constants.KEY_DONATION_ID));

        if (userId != null) {
            loveListRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        binding.icLove.setImageResource(R.drawable.ic_love);
                    } else {
                        binding.icLove.setImageResource(R.drawable.ic_love_stroke);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void setFragment() {
        donateDetailAdapter = new DonateDetailAdapter(this);
        binding.vP2.setAdapter(donateDetailAdapter);

        binding.vP2.setOffscreenPageLimit(3);

        descriptionFragment = new DescriptionFragment();
        latestnewsFragment = new LatestnewsFragment();
        volunteerFragment = new VolunteerFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Constants.KEY_DONATION_DESC, getIntent().getExtras().getString(Constants.KEY_DONATION_DESC));
        descriptionFragment.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putString("title1", "Title 1");
        latestnewsFragment.setArguments(bundle2);
        donateDetailAdapter.addFragment(descriptionFragment);
        donateDetailAdapter.addFragment(latestnewsFragment);
        donateDetailAdapter.addFragment(volunteerFragment);



        new TabLayoutMediator(binding.tabLayout, binding.vP2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Description");
                    break;
                case 1:
                    tab.setText("Latest News");
                    break;
                case 2:
                    tab.setText("Volunteer");
                    break;
            }
        }).attach();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vP2.setCurrentItem(tab.getPosition());
                adjustViewPagerHeight(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                adjustViewPagerHeight(tab.getPosition());
            }
        });

        binding.vP2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.getTabAt(position).select();
            }
        });

    }

    private void adjustViewPagerHeight(int position) {
        Fragment selectedFragment = donateDetailAdapter.createFragment(position);
        View fragmentView = selectedFragment.getView();

        if (fragmentView != null) {
            fragmentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int fragmentHeight = fragmentView.getMeasuredHeight();

            ViewGroup.LayoutParams layoutParams = binding.vP2.getLayoutParams();
            if(fragmentHeight < 450) {
                layoutParams.height = 500;
            } else {
                layoutParams.height = fragmentHeight;
            }
            layoutParams.width = fragmentView.getWidth();
            binding.vP2.setLayoutParams(layoutParams);
        }
    }

    private void setCountdown (long duration) {
        new CountDownTimer(duration * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", days, hours, minutes);


                        final String[] daysHourMinutes = time.split(":");
                        binding.days.setText(daysHourMinutes[0] + "d");
                        binding.hours.setText(daysHourMinutes[1] + "h");
                        binding.minutes.setText(daysHourMinutes[2] + "m");

                    }
                });
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void imageSlider() {
        List<SlideModel> slideModels = new ArrayList<>();
        if(getIntent().getExtras().containsKey(Constants.KEY_IMAGE_URL1)) {
            slideModels.add(new SlideModel(getIntent().getExtras().getString(Constants.KEY_IMAGE_URL1), null, ScaleTypes.FIT));
        }
        if(getIntent().getExtras().containsKey(Constants.KEY_IMAGE_URL2)) {
            slideModels.add(new SlideModel(getIntent().getExtras().getString(Constants.KEY_IMAGE_URL2), null, ScaleTypes.FIT));
        }
        binding.imageSlider.setImageList(slideModels);

    }

    private void lovePost() {
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null) {
            DatabaseReference loveListRef = rootRef.child("loveList").child(getIntent().getExtras().getString(Constants.KEY_DONATION_ID));

            loveListRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        binding.icLove.setImageResource(R.drawable.ic_love_stroke);
                        loveListRef.child(userId).removeValue();
                    } else {
                        binding.icLove.setImageResource(R.drawable.ic_love);
                        Toast.makeText(getApplicationContext(), "Saved to Lovelist", Toast.LENGTH_SHORT).show();
                        loveListRef.child(userId).setValue(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}