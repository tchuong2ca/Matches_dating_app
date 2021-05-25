package com.example.matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class Profile_Activity extends AppCompatActivity {
    private TextView logout;
    private static final int ACT_NUM = 2;
    static boolean active = false;
    private FirebaseAuth mauth;
    private Context mContext = com.example.matches.Profile_Activity.this;
    private ImageView imagePerson;
    public TextView name;
    public String gender;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private String userId,profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        checkgender();

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        setupNav();

        mauth = FirebaseAuth.getInstance();
        imagePerson = findViewById(R.id.circle_profile_image);
        name = findViewById(R.id.profile_name);
        auth= FirebaseAuth.getInstance();
        userId= auth.getCurrentUser().getUid();
        logout = findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        ImageButton edit_btn = findViewById(R.id.edit_profile);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, edit_profile.class);
                startActivity(intent);
                return;
            }
        });

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Profile.Profile_Activity.this, SettingsActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void getimg() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>)snapshot.getValue();
                    if(map.get("imgUrl")!=null){
                        profileImageUrl = map.get("imgUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.userlogo).into(imagePerson);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(imagePerson);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Logout() {
        mauth.signOut();
        Intent intent = new Intent(Profile_Activity.this, Login_Activity.class);
        startActivity( intent);
        finish();
        return;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void setupNav() {
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        Navigation.setupNavigation(tvEx);
        Navigation.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACT_NUM);
        menuItem.setChecked(true);
    }
    @Override
    public void onBackPressed() {
    }
    public void checkgender(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference udb = databaseReference.child(user.getUid()) ;
        udb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey().equals(user.getUid())){
                if(snapshot.exists()){
                    if (snapshot.child("gender").getValue()!=null){
                        gender=snapshot.child("gender").getValue().toString();
                        switch (gender){
                            case "male":
                            case "female":
                                name.setText(snapshot.child("name").getValue().toString());
                                getimg();
                                break;
                        }
                    }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
