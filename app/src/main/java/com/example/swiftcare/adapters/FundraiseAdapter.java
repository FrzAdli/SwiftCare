package com.example.swiftcare.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.swiftcare.fragments.FundraiseFragment1;
import com.example.swiftcare.fragments.FundraiseFragment2;

import java.util.ArrayList;
import java.util.List;

public class FundraiseAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public FundraiseAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FundraiseFragment1();
            case 1:
                return new FundraiseFragment2();

            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
