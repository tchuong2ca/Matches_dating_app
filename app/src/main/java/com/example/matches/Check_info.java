package com.example.matches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class Check_info extends AppCompatActivity
{

    Button back;
    private Context mContext;
    String profileImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_info);

        mContext = Check_info.this;

        back = findViewById(R.id.exit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView profileName = findViewById(R.id.name_main);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileAge = findViewById(R.id.age_main);
        TextView profileDes = findViewById(R.id.bio);
        TextView proContact = findViewById(R.id.contact);
        TextView proHob = findViewById(R.id.hobbies);
        TextView proAdd = findViewById(R.id.distance_main);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        String description = intent.getStringExtra("description");
        String contact = intent.getStringExtra("contact");
        String hobbies = intent.getStringExtra("hobbies");
        String address = intent.getStringExtra("address");


        profileAge.setText(age);
        profileName.setText(name);
        profileDes.setText(description);
        proContact.setText(contact);
        proContact.setMovementMethod(LinkMovementMethod.getInstance());
        proHob.setText(hobbies);
        proAdd.setText(address);

        profileImageUrl = intent.getStringExtra("photo");
        if ("default".equals(profileImageUrl)) {
            Picasso.get().load(R.drawable.userlogo).into(profileImage);
        } else {
            Picasso.get().load(profileImageUrl).into(profileImage);
        }
    }
}
