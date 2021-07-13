package com.example.pixelwallpaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpMainActivity extends AppCompatActivity {

    EditText email,userN,fullN,pass,cPass;
    Button SignUp;
    String EMail,UserName,FullName,Password,cPassword,phone;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    ProgressDialog pd;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);

        back = findViewById(R.id.buttonback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email = findViewById(R.id.eMail);
        userN = findViewById(R.id.userName);
        fullN = findViewById(R.id.fullName);
        pass = findViewById(R.id.EnterPassword);
        cPass = findViewById(R.id.confirmPassword);
        SignUp = findViewById(R.id.SignUpMain);

        pd = new ProgressDialog(this);

    }

    public void registerUser(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        EMail = email.getText().toString().trim();
        UserName = userN.getText().toString();
        FullName = fullN.getText().toString();
        Password = pass.getText().toString().trim();
        cPassword = cPass.getText().toString().trim();
        phone = getIntent().getStringExtra("phone02");

        if(UserName.contains("@") || UserName.contains("*") || UserName.contains("-") || UserName.contains(",") || UserName.contains("/") || UserName.contains(" ")){
            Toast.makeText(SignUpMainActivity.this, "Username can only use letters,numbers,underscores and periods", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Password.length()<6){
            Toast.makeText(SignUpMainActivity.this, "Password too Week!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Password.equals(cPassword)){
            Toast.makeText(SignUpMainActivity.this, "Confirm Password field is not Matched", Toast.LENGTH_SHORT).show();
            return;
        }


        pd.setMessage("Please Wail!");
        pd.show();

        firebaseAuth.createUserWithEmailAndPassword(EMail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                  //  UnqId = firebaseAuth.getUid();
                  //  UserHelperClass helperClass = new UserHelperClass(EMail,UserName,Password,phone,UnqId);
                  //  reference.child(helperClass.getUsername()).setValue(helperClass);

                    HashMap<String , Object> map = new HashMap<>();
                    map.put("username" , UserName);
                    map.put("email", EMail);
                    map.put("fullname", FullName);
                    map.put("password", Password);
                    map.put("phonenumber", phone);
                    map.put("id" , firebaseAuth.getCurrentUser().getUid());
                    map.put("bio" , "");
                    map.put("imageurl" , "default");

                    reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(map);

                    pd.dismiss();
                    Toast.makeText(SignUpMainActivity.this, "You are all setup.\n Redirecting to Login page...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),loginActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(SignUpMainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpMainActivity.this, "Error:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(SignUpMainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        })

        ;

    }

    public void Login(View view) {

        startActivity(new Intent(this,LoginMainActivity.class));
    }



}