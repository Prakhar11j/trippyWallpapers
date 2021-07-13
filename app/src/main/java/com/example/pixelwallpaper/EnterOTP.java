package com.example.pixelwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {

    EditText otp;
    Button VerifyBtn;
    ProgressBar progressBar;
    String codeSentBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);

        otp = findViewById(R.id.enterOtp);
        VerifyBtn = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        String phoneNo = getIntent().getStringExtra("phone01");

        sendVerificationCode(phoneNo);

    //Manual process ie. if number is not in the phone....
        VerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = otp.getText().toString();
                if(a.isEmpty() || a.length()<6){
                    otp.setError("Wrong otp...");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                    verifyCode();
            }
        });
    }
    //Automatic process ie. if number is in the phone....
    private void sendVerificationCode(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSentBySystem = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(EnterOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyCode(){

        Intent i = new Intent(getApplicationContext(),SignUpMainActivity.class);
        i.putExtra("phone02",getIntent().getStringExtra("phone01"));
        startActivity(i);
        finish();
    }

   // private void signInByCredential(PhoneAuthCredential credential) {

     //   Intent i = new Intent(getApplicationContext(),SignUpMainActivity.class);
     //  i.putExtra("phone02",getIntent().getStringExtra("phone01"));
     //   startActivity(i);
     //   finish();

    //}
}