package com.example.brijesh.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.brijesh.R;
import com.example.brijesh.adapter.AdapterPosts;
import com.example.brijesh.adapter.AdapterStory;
import com.example.brijesh.model.ModelPost;
import com.example.brijesh.model.Story;
import com.example.brijesh.model.UsersStories;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    String myuid;
    ImageView dp;
    ShimmerRecyclerView recyclerView,storyRv;
    List<ModelPost> posts;
    ArrayList<Story> storyList;
    AdapterPosts adapterPosts;
    RoundedImageView addStory;
    ActivityResultLauncher<String> galaryLauncher;
    ProgressDialog pd;




    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.postrecyclerview);
        storyRv = view.findViewById(R.id.sroryRV);
        dp = view.findViewById(R.id.prodp);
        //&&&&&&&&&&&&&     SHIMER EFECT &&&&&&&&&&&&&&&&&
        recyclerView.showShimmerAdapter();
        storyRv.showShimmerAdapter();
        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Story Uploding");
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        recyclerView.setHasFixedSize(true);
        storyRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager1.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager1.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        storyRv.setLayoutManager(layoutManager1);
        storyRv.setNestedScrollingEnabled(false);
        posts = new ArrayList<>();
        storyList = new ArrayList<>();
        AdapterStory adapterStory = new AdapterStory(storyList, getContext());

        //*********************************************
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    storyList.clear();
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        Story story = new Story();
                        story.setStoryBy(snapshot1.getKey());
                        story.setStoryAt(snapshot1.child("postedBy").getValue(long.class));

                        ArrayList<UsersStories> stories = new ArrayList<>();
                        for (DataSnapshot snapshot2: snapshot1.child("userStories").getChildren()){
                            UsersStories usersStories = snapshot2.getValue(UsersStories.class);
                            long t1= usersStories.getStoryAtSto()+86400000;
                            long t2= new Date().getTime();

                            if (t1>=t2) {
                                stories.add(usersStories);
                            }else {

                            }

                        }
                        story.setStories(stories);
                        storyList.add(story);
                    }
                    storyRv.setAdapter(adapterStory);
                    adapterStory.notifyDataSetChanged();
                    storyRv.hideShimmerAdapter();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //*******************************************

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase

                    String image = "" + dataSnapshot1.child("image").getValue();


                    // setting data to our text view

                    try {
                        Glide.with(getActivity()).load(image).placeholder(R.drawable.profile_image).into(dp);
                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //***************************************

        loadPosts();
        //8888888888888888888888888888888888888888888888888
        addStory = view.findViewById(R.id.post_home_img);
        addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              galaryLauncher.launch("image/*");




            }
        });
        galaryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                   addStory.setImageURI(result);
                   pd.show();

                   final StorageReference reference = storage.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime()+"");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Story story = new Story();
                                        story.setStoryAt(new Date().getTime());
                                        database.getReference()
                                                .child("stories")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                UsersStories stories = new UsersStories(uri.toString(), story.getStoryAt());
                                                database.getReference().child("stories")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("userStories")
                                                        .push()
                                                        .setValue(stories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        pd.dismiss();
                                                        Toast.makeText(getContext(), "Uploded", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });

        //888888888888888888888888888888888888888888888888888888888



        return view;
    }

    private void loadPosts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelPost modelPost = dataSnapshot1.getValue(ModelPost.class);

                   /* FirebaseDatabase.getInstance().getReference("Users")
                            .child(modelPost.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ModelUsers users1 = snapshot.getValue(ModelUsers.class);
                                    //modelPost.setUdp(users1.getImage());
                                    //modelPost.setUname(users1.getName());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/
                    posts.add(modelPost);
                    adapterPosts = new AdapterPosts(getActivity(), posts);
                    recyclerView.setAdapter(adapterPosts);
                }
                recyclerView.hideShimmerAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    // Search post code
    private void searchPosts(final String search) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelPost modelPost = dataSnapshot1.getValue(ModelPost.class);
                    if (modelPost.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                            modelPost.getDescription().toLowerCase().contains(search.toLowerCase())) {
                        posts.add(modelPost);
                    }
                    adapterPosts = new AdapterPosts(getActivity(), posts);
                    recyclerView.setAdapter(adapterPosts);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.logout).setVisible(false);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchPosts(query);
                } else {
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    searchPosts(newText);
                } else {
                    loadPosts();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }





}
