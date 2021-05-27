package com.example.matches;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Registration_Activity extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail, mPassword, mName;
    boolean male=true;
    private RadioGroup mRadioGroup;
    private TextView logintext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(Registration_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mRegister = findViewById(R.id.btn_regis);
logintext = findViewById(R.id.link_login);
        mEmail = findViewById(R.id.signup_mail);
        mPassword = findViewById(R.id.signup_pwd);
        mName = findViewById(R.id.signup_name);


        mRadioGroup = findViewById(R.id.radioGroup);
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = findViewById(selectId);

                if(radioButton.getText() == null){
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registration_Activity.this,
                                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){

                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                Toast.makeText(Registration_Activity.this, "weak password", Toast.LENGTH_SHORT).show();
                                // TODO: take your actions!
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                            {
                                Toast.makeText(Registration_Activity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Toast.makeText(Registration_Activity.this, "This email address is already being used", Toast.LENGTH_SHORT).show();

                                // TODO: Take your action
                            }
                            catch (Exception e)
                            {
                            }
                        }
                        else{
                            Toast.makeText(Registration_Activity.this, "Successfully, automating login...", Toast.LENGTH_SHORT).show();
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("imgUrl", "default");
                            userInfo.put("gender",radioButton.getText().toString());
                            userInfo.put("proAge", "0");
                            userInfo.put("proDes", "not set yet");
                            userInfo.put("proAdd", "not set yet");
                            userInfo.put("proHob", "not set yet");
                            userInfo.put("proContact", "not set yet");
                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
