package com.example.swiftcare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.swiftcare.R;
import com.example.swiftcare.databinding.ActivityMainPageBinding;
import com.example.swiftcare.fragments.DonateFragment;
import com.example.swiftcare.fragments.HomeFragment;
import com.example.swiftcare.fragments.LovelistFragment;
import com.example.swiftcare.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    ActivityMainPageBinding binding;

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Navbar();


    }

    private void Navbar(){
        LinearLayout HomeLayout = binding.HomeLayout;
        LinearLayout DonateLayout = binding.DonateLayout;
        LinearLayout LoveListLayout= binding.LovelistLayout;
        LinearLayout ProfileLayout = binding.ProfileLayout;

        ImageView ImageHome = binding.HomeIcon;
        ImageView ImageDonate = binding.ImageDonate;
        ImageView ImageLovelist = binding.LovelistIcon;
        ImageView ImageProfile= binding.ProfileIcon;


        TextView HomeTxt = binding.HomeTxt;
        TextView DonateTxt = binding.DonateTxt;
        TextView LovelistTxt = binding.LovelistTxt;
        TextView ProfileTxt = binding.ProfileTxt;

        //set home fragment by default
        getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, HomeFragment.class, null)
                        .commit();

        HomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 1) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class, null)
                            .commit();

                    DonateTxt.setVisibility(View.GONE);
                    LovelistTxt.setVisibility(View.GONE);
                    ProfileTxt.setVisibility(View.GONE);

                    ImageDonate.setImageResource(R.drawable.ic_donate_green);
                    ImageLovelist.setImageResource(R.drawable.ic_lovelist_green);
                    ImageProfile.setImageResource(R.drawable.ic_profile_green);

                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);
                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected home tab
                    HomeTxt.setVisibility(View.VISIBLE);
                    ImageHome.setImageResource(R.drawable.ic_home_clean);
                    HomeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    HomeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        DonateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 2) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, DonateFragment.class, null)
                            .commit();

                    HomeTxt.setVisibility(View.GONE);
                    LovelistTxt.setVisibility(View.GONE);
                    ProfileTxt.setVisibility(View.GONE);

                    ImageHome.setImageResource(R.drawable.ic_home_green);
                    ImageLovelist.setImageResource(R.drawable.ic_lovelist_green);
                    ImageProfile.setImageResource(R.drawable.ic_profile_green);

                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected Donate tab
                    DonateTxt.setVisibility(View.VISIBLE);
                    ImageDonate.setImageResource(R.drawable.ic_donate_clean);
                    DonateLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    DonateLayout.startAnimation(scaleAnimation);

                    selectedTab =2;

                }
            }
        });
        LoveListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 3) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, LovelistFragment.class, null)
                            .commit();

                    HomeTxt.setVisibility(View.GONE);
                    DonateTxt.setVisibility(View.GONE);
                    ProfileTxt.setVisibility(View.GONE);

                    ImageHome.setImageResource(R.drawable.ic_home_green);
                    ImageDonate.setImageResource(R.drawable.ic_donate_green);
                    ImageProfile.setImageResource(R.drawable.ic_profile_green);

                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);
                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected Lovelist tab
                    LovelistTxt.setVisibility(View.VISIBLE);
                    ImageLovelist.setImageResource(R.drawable.ic_lovelist_clean);
                    LoveListLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    LoveListLayout.startAnimation(scaleAnimation);

                    selectedTab =3;
                }
            }
        });
        ProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 4) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class, null)
                            .commit();
                    HomeTxt.setVisibility(View.GONE);
                    LovelistTxt.setVisibility(View.GONE);
                    DonateTxt.setVisibility(View.GONE);

                    ImageHome.setImageResource(R.drawable.ic_home_green);
                    ImageLovelist.setImageResource(R.drawable.ic_lovelist_green);
                    ImageDonate.setImageResource(R.drawable.ic_donate_green);

                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected home tab
                    ProfileTxt.setVisibility(View.VISIBLE);
                    ImageProfile.setImageResource(R.drawable.ic_profile_clean);
                    ProfileLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    ProfileLayout.startAnimation(scaleAnimation);


                    selectedTab = 4;
                }
            }
        });


    }
}