package com.example.brijesh;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Zoomimg extends AppCompatActivity {

    ImageView zoom;
    String imgg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomimg);

    zoom = findViewById(R.id.imagezoom);
    imgg = getIntent().getStringExtra("img");
        try {
            Glide.with(Zoomimg.this).load(imgg).placeholder(R.drawable.profile_image).into(zoom);
        } catch (Exception e) {

        }
    }
}