package com.example.brijesh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.brijesh.adapter.AllPostAdapter;
import com.example.brijesh.databinding.ActivityUserProfileBinding;
import com.example.brijesh.model.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {


    String uId,uDp,uName,uBio,uEmail,uCover;
    ActivityUserProfileBinding binding;
    Context context;
    ArrayList<ModelPost> userposts;
    AllPostAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("User Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        uId = intent.getStringExtra("uid");
        uDp= intent.getStringExtra("udp");
        uName = intent.getStringExtra("uname");
        uBio = intent.getStringExtra("ubio");
        uEmail = intent.getStringExtra("uemail");
        uCover = intent.getStringExtra("ucover");

        binding.nametvUser.setText(uName);
        binding.userBio.setText(uBio);



        try {
            Glide.with(UserProfileActivity.this).load(uDp).placeholder(R.drawable.profile_image).into(binding.userDp);
        } catch (Exception e) {

        }
        try {
            Glide.with(UserProfileActivity.this).load(uCover).placeholder(R.drawable.daisy).into(binding.userCover);
        } catch (Exception e) {

        }


        userposts = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(UserProfileActivity.this,3);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        binding.userProfileRv.setLayoutManager(layoutManager);

        //**********************
        loadPosts();
        //********************




    }
    //*************************************************************

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }


    private void checkOnlineStatus(String status) {
        // check online status
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);
        dbref.updateChildren(hashMap);
    }
//8888888888888888888888888888888888888888888888

    private void loadPosts() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userposts.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelPost modelPost = dataSnapshot1.getValue(ModelPost.class);
                    if (uId.equals(modelPost.getUid())){
                        userposts.add(modelPost);
                    }
                    adapter = new AllPostAdapter(UserProfileActivity.this, userposts);
                    binding.userProfileRv.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UserProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}