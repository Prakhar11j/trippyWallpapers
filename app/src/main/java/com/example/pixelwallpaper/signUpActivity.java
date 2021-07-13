package com.example.pixelwallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class signUpActivity extends AppCompatActivity {

    Button otp;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        phone = findViewById(R.id.enterPhoneNumber);
        otp = findViewById(R.id.getOtp);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),EnterOTP.class);
                i.putExtra("phone01",phone.getText().toString());
                startActivity(i);
            }
        });
    }

    public void Login(View view) {
        startActivity(new Intent(this,LoginMainActivity.class));
    }

    public void SignUpAgain(View view) {
        startActivity((new Intent(this,EnterOTP.class)));
    }
}