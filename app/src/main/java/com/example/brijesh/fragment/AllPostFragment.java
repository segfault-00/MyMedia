package com.example.brijesh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brijesh.R;
import com.example.brijesh.adapter.AllPostAdapter;
import com.example.brijesh.model.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllPostFragment extends Fragment {

    ArrayList<ModelPost> allposts;
    AllPostAdapter adapterPosts;
    RecyclerView postrecycle;


    public AllPostFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_post, container, false);

        postrecycle = view.findViewById(R.id.recyclerposts);
        allposts = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        postrecycle.setLayoutManager(layoutManager);

        //**********************
       loadPosts();
        //********************



        return view;
    }
    private void loadPosts() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allposts.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelPost modelPost = dataSnapshot1.getValue(ModelPost.class);
                    if (FirebaseAuth.getInstance().getUid().equals(modelPost.getUid())){
                        allposts.add(modelPost);
                    }
                    adapterPosts = new AllPostAdapter(getActivity(), allposts);
                    postrecycle.setAdapter(adapterPosts);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}