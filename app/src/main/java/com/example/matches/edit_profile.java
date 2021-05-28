package com.example.matches;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Delayed;

public class edit_profile extends AppCompatActivity
{
    private EditText description, age, contact, address,hobbies;
    private Button confirm;
    private ImageButton back;
    private ImageView mProfileImage;
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private    String userid, proDes, proAge, proContact, proAdd, imgUrl,proHobbies,gender;
    private Uri result;

    public Button selectimg;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        description = findViewById(R.id.description);
        age = findViewById(R.id.age);
        contact = findViewById(R.id.update_contact);
        address = findViewById(R.id.address);
        mProfileImage = findViewById(R.id.profile_picture);
        hobbies= findViewById(R.id.update_hobbies);

        selectimg= findViewById(R.id.selected);

        confirm= findViewById(R.id.confirm);
        back= findViewById(R.id.back);

        auth= FirebaseAuth.getInstance();
        userid= auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        getinfo();
        mProfileImage.setOnClickListener((View v) -> {
            chooseImg();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              saveinfo();
            }
        });
    }
    private void chooseImg() {
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null)
        {
            final Uri ri = data.getData();
            result =ri;
            mProfileImage.setImageURI(result);
        }
    }
    private void getinfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>)snapshot.getValue();
                    if(map.get("proDes")!=null){
                        proDes=map.get("proDes").toString();
                        description.setText(proDes);

                    }
                    if(map.get("proAge")!=null){
                        proAge=map.get("proAge").toString();
                        age.setText(proAge);
                    }
                    if(map.get("proContact")!=null){
                        proContact=map.get("proContact").toString();
                        contact.setText(proContact);
                    }
                    if(map.get("gender")!=null){
                        gender=map.get("gender").toString();
                    }
                    if(map.get("proAdd")!=null){
                        proAdd=map.get("proAdd").toString();
                        address.setText(proAdd);
                    }
                    if(map.get("proHob")!=null){
                        proHobbies=map.get("proHob").toString();
                        hobbies.setText(proHobbies);
                    }
                         Glide.with(getApplicationContext()).clear(mProfileImage);
                    if(map.get("imgUrl")!=null){
                        imgUrl = map.get("imgUrl").toString();
                        if ("default".equals(imgUrl)) {
                            Picasso.get().load(R.drawable.userlogo).into(mProfileImage);
                        } else {
                            Picasso.get().load(imgUrl).into(mProfileImage);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void saveinfo() {
        Toast.makeText(edit_profile.this,"Uploaded",Toast.LENGTH_LONG);
        proDes = description.getText().toString();
        proAge = age.getText().toString();
        proContact = contact.getText().toString();
        proAdd = address.getText().toString();
        proHobbies=hobbies.getText().toString();
        Map userInfo =  new HashMap();
        userInfo.put("proDes",proDes);
        userInfo.put("proAge",proAge);
        userInfo.put("proContact",proContact);
        userInfo.put("proAdd",proAdd);
        userInfo.put("proHob",proHobbies);
        databaseReference.updateChildren(userInfo);

        if(result !=null){
            ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            String img_name = UUID.randomUUID().toString();
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("image/"+img_name).child(userid);

            filepath.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(edit_profile.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map userInfo = new HashMap();
                            userInfo.put("imgUrl", uri.toString());
                            databaseReference.updateChildren(userInfo);
                        }
                    });

                }
            }).addOnFailureListener(e -> {
                mDialog.dismiss();
                Toast.makeText(edit_profile.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                mDialog.setMessage("Uploading "+progress+"%");
            });

        }
        Toast.makeText(edit_profile.this,"Uploaded",Toast.LENGTH_LONG);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(edit_profile.this, MainActivity.class);
                startActivity(intent);
            }
        }, 4000);
    }
}
