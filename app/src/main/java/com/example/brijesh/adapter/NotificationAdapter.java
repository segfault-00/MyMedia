package com.example.brijesh.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brijesh.PostDetailsActivity;
import com.example.brijesh.R;
import com.example.brijesh.databinding.RowNotificatinBinding;
import com.example.brijesh.model.ModelUsers;
import com.example.brijesh.model.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_notificatin, parent, false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        NotificationModel notification = list.get(position);
        String type = notification.getType();

        String at = String.valueOf(notification.getNotificationAt());
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(at));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();



        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModelUsers user = snapshot.getValue(ModelUsers.class);
                        try {
                            Glide.with(context).load(user.getImage()).placeholder(R.drawable.rose).into(holder.binding.uimage);
                        } catch (Exception e){

                        }
                        if (type.equals("like")){
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName()+"</b>" + " liked your post"));
                        } else {
                            holder.binding.notification.setText(Html.fromHtml("<b>"+user.getName()+"</b>" + " comment on your post"));
                        }
                        holder.binding.time.setText(timedate);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.notificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference()
                        .child("notification")
                        .child(notification.getPostedBy())
                        .child(notification.getNotificationId())
                        .child("checkOpen")
                        .setValue(true);
                holder.binding.notificationItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("pid", notification.getPostId());
                intent.putExtra("pBy", notification.getPostedBy());
                context.startActivity(intent);
            }
        });
        Boolean checkOpen = notification.isCheckOpen();
        if (checkOpen == true){
            holder.binding.notificationItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {

        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        RowNotificatinBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
binding = RowNotificatinBinding.bind(itemView);




        }
    }
}
