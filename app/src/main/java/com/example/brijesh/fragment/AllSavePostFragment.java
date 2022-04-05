package com.example.brijesh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.brijesh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AllSavePostFragment extends Fragment {
    TextView abut,name,dates;
    FirebaseDatabase database;

    public AllSavePostFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_save_post, container, false);
        database = FirebaseDatabase.getInstance();
        name = view.findViewById(R.id.name);
        abut = view.findViewById(R.id.about);
        dates = view.findViewById(R.id.date);
        //*********************************************
        database.getReference().child("about").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String devoloper = "" + dataSnapshot1.child("devoloper").getValue();
                    String date = "" + dataSnapshot1.child("abt").getValue();
                    String about = "" + dataSnapshot1.child("datelaunched").getValue();

                    // setting data to our text view
                    name.setText(devoloper);
                    abut.setText(about);
                    dates.setText(date);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //*******************************************
        return view;
    }

}