package com.example.brijesh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.brijesh.R;
import com.example.brijesh.adapter.NotificationViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;





    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new NotificationViewPagerAdapter(getChildFragmentManager()));
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }






}