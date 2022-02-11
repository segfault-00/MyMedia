package com.example.brijesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brijesh.R;
import com.example.brijesh.databinding.RowStoryBinding;
import com.example.brijesh.model.ModelUsers;
import com.example.brijesh.model.Story;
import com.example.brijesh.model.UsersStories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class AdapterStory extends RecyclerView.Adapter<AdapterStory.viewHolder> {
    ArrayList<Story> list;
    Context context;

    public AdapterStory(ArrayList<Story> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_story, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Story story = list.get(position);
        if (story.getStories().size() > 0) {


            UsersStories lastStory = story.getStories().get(story.getStories().size() - 1);
            try {
                Glide.with(context).load(lastStory.getImageSto()).placeholder(R.drawable.rose).into(holder.binding.storyImg);
            } catch (Exception e) {

            }
            holder.binding.storyCricle.setPortionsCount(story.getStories().size());

            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(story.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ModelUsers users = snapshot.getValue(ModelUsers.class);
                            try {
                                Glide.with(context).load(users.getImage()).placeholder(R.drawable.rose).into(holder.binding.uimage);
                            } catch (Exception e) {

                            }
                            holder.binding.name.setText(users.getName());

                            holder.binding.storyImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (UsersStories stories : story.getStories()) {
                                        myStories.add(new MyStory(
                                                stories.getImageSto()

                                        ));
                                    }
                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(users.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(users.getImage()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();


                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        RowStoryBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowStoryBinding.bind(itemView);

        }
    }
}
