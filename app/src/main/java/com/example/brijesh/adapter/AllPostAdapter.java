package com.example.brijesh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brijesh.PostDetailsActivity;
import com.example.brijesh.R;
import com.example.brijesh.model.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.viewHolder>{
    Context context;
    ArrayList<ModelPost> allposts;
    String myuid;
    private DatabaseReference liekeref, postref;

    public AllPostAdapter(Context context, ArrayList<ModelPost> allposts) {
        this.context = context;
        this.allposts = allposts;
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_post_row,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final String uid = allposts.get(position).getUid();
        final String image = allposts.get(position).getUimage();
        final String ptime = allposts.get(position).getPtime();
        try {
            Glide.with(context).load(image).placeholder(R.drawable.rose).into(holder.allPost);
        } catch (Exception e) {

        }
        holder.allPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("pid", ptime);
                    intent.putExtra("pBy",uid);
                    context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allposts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView allPost;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            allPost = itemView.findViewById(R.id.allPost);
        }
    }
}
