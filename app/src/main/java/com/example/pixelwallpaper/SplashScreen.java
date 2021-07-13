package com.example.pixelwallpaper;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    FirebaseAuth firebaseAuth;
    FirebaseUser abcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash_screen);
        abcd = firebaseAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(abcd!=null){
                    Intent s = new Intent(getApplicationContext(),MainActivity.class);
                    s.putExtra("k",abcd.getEmail().toString());
                    startActivity(s);
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(),loginActivity.class));
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }


}