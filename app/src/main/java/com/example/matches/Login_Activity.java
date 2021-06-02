package com.example.matches;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;


public class Login_Activity extends AppCompatActivity {

    private Button mLogin;
    private EditText mEmail, mPassword;
    private TextView registext;
    private FirebaseAuth mAuth;
TextView forgotpwd;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acivity);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user !=null){
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mLogin = findViewById(R.id.login);
        registext= findViewById(R.id.link_signup);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        forgotpwd = findViewById(R.id.forgotPwd);
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,ForgotPassword_Activity.class));
            }
        });

        registext.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Activity.this,Registration_Activity.class);
            startActivity(intent);
        });

        mLogin.setOnClickListener(view -> {
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();

            if(email.isEmpty()){
                Toast.makeText(Login_Activity.this, "Please enter your email ", Toast.LENGTH_SHORT).show();
            }
            else if(password.isEmpty()){
                Toast.makeText(Login_Activity.this, "Please enter your password ", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login_Activity.this, task -> {
                    if(!task.isSuccessful()){

                        try
                        {
                            throw task.getException();
                        }
                        // if user enters wrong email.
                        catch (FirebaseAuthInvalidUserException invalidEmail)
                        {
                            Toast.makeText(Login_Activity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        }
                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                        {
                            Toast.makeText(Login_Activity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                        }
                    }
                });
            }
        });
    }
//    private void sendEmailVerification() {
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Email sent
//                    }
//                });
//        // [END send_email_verification]
//    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(firebaseAuthStateListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    private void reload() { }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    public void onBackPressed() {
    }
}