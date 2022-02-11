package com.example.brijesh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.brijesh.fragment.ChatlistFragment;
import com.example.brijesh.fragment.NotifiFragment;

public class NotificationViewPagerAdapter extends FragmentStatePagerAdapter {
    public NotificationViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position){
            case 0: return new NotifiFragment();
            case 1: return new ChatlistFragment();
            default: return new NotifiFragment();
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0){
            title = "NOTIFICATION";
        }else if (position == 1){
            title = "CHATLIST";
        }

        return title;
    }
}
