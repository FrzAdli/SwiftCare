package com.example.swiftcare.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.swiftcare.fragments.DescriptionFragment;
import com.example.swiftcare.fragments.LatestnewsFragment;
import com.example.swiftcare.fragments.VolunteerFragment;

public class DonateDetailAdapter extends FragmentStateAdapter {
    public DonateDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new LatestnewsFragment();
            case 2:
                return new VolunteerFragment();
            default:
                return new DescriptionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
