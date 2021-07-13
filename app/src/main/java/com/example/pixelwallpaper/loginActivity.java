package com.example.pixelwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (Button)findViewById(R.id.buttonLogin);
    }

    public void jumpToLogin(View view) {
        startActivity(new Intent(this,LoginMainActivity.class));
        finish();
    }

    public void SignUp(View view) {

        startActivity((new Intent(this,signUpActivity.class)));
    }
}