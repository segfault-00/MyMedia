package com.example.brijesh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brijesh.ChatActivity;
import com.example.brijesh.R;
import com.example.brijesh.UserProfileActivity;
import com.example.brijesh.model.ModelUsers;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {

    Context context;
    FirebaseAuth firebaseAuth;
    String uid;

    public AdapterUsers(Context context, List<ModelUsers> list) {
        this.context = context;
        this.list = list;
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    List<ModelUsers> list;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        ModelUsers user = list.get(position);
        final String hisuid = user.getUid();
        String userImage = user.getImage();
        String userecover = user.getCover();
        String username = user.getName();
        String userbio = user.getBio();
        String useremail = user.getEmail();
        String onlin = user.getOnlineStatus();
        holder.name.setText(username);
        holder.email.setText(userbio);
        if (onlin.equals("online")){
        holder.onli.setVisibility(View.VISIBLE);
        }
        try {
            Glide.with(context).load(userImage).placeholder(R.drawable.profile_image).into(holder.profiletv);
        } catch (Exception e) {
        }

        //****************************
        holder.profiletv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), UserProfileActivity.class);
                intent.putExtra("uid", hisuid);
                intent.putExtra("udp", userImage);
                intent.putExtra("uname",username);
                intent.putExtra("ubio",userbio);
                intent.putExtra("uemail",useremail);
                intent.putExtra("ucover",userecover);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        //****************************
        //&&&&&&&&&&&&&&&&&&&&&&&&&&&
        // redirecting to chat activity on item click
        holder.masseg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);

                // putting uid of user in extras
                intent.putExtra("uid", hisuid);
                context.startActivity(intent);
            }
        });
        //&&&&&&&&&&&&&&&&&&&&&&&&&&


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView profiletv;
        TextView name, email;
        ImageView masseg;
        View onli;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profiletv = itemView.findViewById(R.id.imagep);
            name = itemView.findViewById(R.id.namep);
            email = itemView.findViewById(R.id.emailp);
            masseg = itemView.findViewById(R.id.massageBtn);
            onli = itemView.findViewById(R.id.online);
        }
    }
}