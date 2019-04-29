package com.example.g2_stockprediction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {
    private Button signup;
    private EditText username;
    private EditText password;
    private EditText passwordcheck;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        signup = (Button) findViewById(R.id.SignUp);
        username = (EditText) findViewById(R.id.usernaame);
        password = (EditText) findViewById(R.id.password1);
        passwordcheck = (EditText) findViewById(R.id.password2);
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernamein = username.getText().toString();
                String passwordin = password.getText().toString();
                String passwordcheckin = passwordcheck.getText().toString();
                if (!passwordin.equals(passwordcheckin)) {
                    Toast.makeText(SignUpPage.this, "Your password does not match",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        mAuth.createUserWithEmailAndPassword(usernamein, passwordin)
                                .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(SignUpPage.this, "SignUp success",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpPage.this, LogInPage.class);
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUpPage.this, "SignUp failed. Account already exist or invalid email.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(SignUpPage.this, "Please enter email and password",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
