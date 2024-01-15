package com.example.swiftcare.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.swiftcare.fragments.DescriptionFragment;
import com.example.swiftcare.fragments.LatestnewsFragment;
import com.example.swiftcare.fragments.VolunteerFragment;

import java.util.ArrayList;
import java.util.List;

public class DonateDetailAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public DonateDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        while (fragmentList.size() <= position) {
            fragmentList.add(null);
        }

        if (fragmentList.get(position) == null) {
            switch (position) {
                case 0:
                    fragmentList.set(position, new DescriptionFragment());
                    break;
                case 1:
                    fragmentList.set(position, new LatestnewsFragment());
                    break;
                case 2:
                    fragmentList.set(position, new VolunteerFragment());
                    break;
            }
        }

        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
