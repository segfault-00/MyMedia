package com.example.brijesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    // variables *********************************
    FirebaseUser currentUser;                    // currentUser
    private FirebaseAuth mAuth;                  // firebaseAuth reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //******************************************
        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null) {
            currentUser = mAuth.getCurrentUser();
        }
        // Tase sedule using Handler and rurable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    Intent intent = new Intent(SplashScreen.this , LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this , DashboardActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       //  if once app open then alwawes open Dashbord Activity
                    startActivity(mainIntent);
                    finish();
                }
            }
        },2000);


    }
}