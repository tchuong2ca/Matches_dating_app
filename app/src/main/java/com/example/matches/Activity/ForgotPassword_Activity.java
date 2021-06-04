package com.example.matches.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matches.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_Activity extends AppCompatActivity {

    EditText inputresetmail;
    Button resetpwd;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    TextView backtoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpwd_main);

        inputresetmail = findViewById(R.id. resetmail_input);
        resetpwd = findViewById(R.id.resetpwd);
        progressBar= findViewById(R.id.progressbar);
        backtoLogin=findViewById(R.id.backtoLogin);
        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        firebaseAuth = FirebaseAuth.getInstance();
    resetpwd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
resetPassword();
        }
    });
    }

    private void resetPassword() {
        String email = inputresetmail.getText().toString().trim();
        if(email.isEmpty()){
            inputresetmail.setError("Email is require!!");
            inputresetmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputresetmail.setError("Please provide valid email!");
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword_Activity.this ,"Check your email", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ForgotPassword_Activity.this, Login_Activity.class);
                            startActivity(intent);
                        }
                    }, 800);

                }
                else {
                    Toast.makeText(ForgotPassword_Activity.this ,"Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
    }

}
