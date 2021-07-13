package com.example.pixelwallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pixelwallpaper.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class feedbackActivity extends AppCompatActivity {

    TextView rateCount, showRating;
    EditText review;
    RatingBar ratingBar;
    Button submit,back;
    float rateValue; String temp;
    String a,b;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        rateCount = findViewById(R.id.rateCount);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.review);
        submit = findViewById(R.id.btnSubmit);
        back = findViewById(R.id.btnbackFeedback);
        phoneNumber = "+917905865514";

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                rateValue = ratingBar.getRating();
                rateCount.setText(rateValue +" ");
                a = rateCount.getText().toString();

            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                b = user.getFullname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "Hey, this is " + b + " ,User at *Trippy*. \n\n Acc. to me this app is : *"+ a + "/5*. \n\n My Review : *" + review.getText().toString() + "*";
                if(review.getText().toString().isEmpty()){
                    Toast.makeText(feedbackActivity.this, "Add a Feedback", Toast.LENGTH_SHORT).show();
                }else {
                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if(installed){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber
                                + "&text=" + temp));
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "WhatsApp not installed on your Device", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private boolean appInstalledOrNot(String uri){
        PackageManager packageManager = getPackageManager();
        boolean appInstalled;

        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        }catch (PackageManager.NameNotFoundException e){
            appInstalled = false;
        }
        return appInstalled;
    }
}