package com.example.pixelwallpaper;

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

public class LoginMainActivity extends AppCompatActivity {

    Button loginbtn;
    String Email, Password;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        loginbtn = findViewById(R.id.loginMain);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void logInUser(View view) {

        Email = ((EditText)findViewById(R.id.emailLogin)).getText().toString().trim();
        Password = ((EditText)findViewById(R.id.passwordLogin)).getText().toString().trim();

        if(!Email.isEmpty() && !Password.isEmpty()){

            firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginMainActivity.this , MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginMainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginMainActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(LoginMainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(this, "Empty Credentials", Toast.LENGTH_SHORT).show();
        }

    }







    public void SignUp(View view) {
            startActivity(new Intent(this,signUpActivity.class));
    }

    public void back(View view) {
        finish();
    }


}