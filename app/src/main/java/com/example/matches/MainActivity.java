package com.example.matches;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Cards cards[];
    private CardsAdapter cardsAdapter;
    private DatabaseReference userDb;
    private FirebaseAuth auth;
    private Context mContext = MainActivity.this;
    private static final int ACT_NUM = 0;
    private String curentId;


    List<Cards> rowitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        auth=FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        curentId = auth.getCurrentUser().getUid();
        setupNavigation();
        checkgender();
        rowitems = new ArrayList<Cards>();
        cardsAdapter = new CardsAdapter(this, R.layout.item, rowitems );
        SwipeFlingAdapterView flingAdapterView = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingAdapterView.setAdapter(cardsAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                rowitems.remove(0);
                cardsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards cards= (Cards) dataObject;
                String uid = cards.getUserId();
                userDb.child(uid).child("relative").child("nope").child(curentId).setValue("true");
                Toast.makeText(MainActivity.this, "Left!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRightCardExit(Object dataObject) {
                Cards cards= (Cards) dataObject;
                String uid = cards.getUserId();
                userDb.child(uid).child("relative").child("like").child(curentId).setValue("true");
                status(uid);
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });
        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void status(String uid) {
        DatabaseReference statusDb = userDb.child(curentId).child("relative").child("like").child(uid);
        statusDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(MainActivity.this,"It's a Match !!! <3",Toast.LENGTH_LONG).show();
                    userDb.child(snapshot.getKey()).child("relative").child("match").child(curentId).setValue("true");
                    userDb.child(curentId).child("relative").child("match").child(snapshot.getKey()).setValue("true");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public String gender,oppositegender;
    public void checkgender(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference udb = userDb.child(user.getUid()) ;
        udb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey().equals(user.getUid())){
                if(snapshot.exists()){
                    if (snapshot.child("gender").getValue()!=null){
                        gender=snapshot.child("gender").getValue().toString();
                        switch (gender){
                            case "male":
                                oppositegender="female";

                                break;
                            case "female":
                                oppositegender ="male";

                                break;
                        }
                        getoppositegender();
                    }
                }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getoppositegender(){

        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.child("gender").getValue() != null) {
                    if (snapshot.exists() && !snapshot.child("relative").child("nope").hasChild(curentId) && !snapshot.child("relative").child("like").hasChild(curentId)&&snapshot.child("gender").getValue().toString().equals(oppositegender)) {
                        String imgUrl = "default";
                       // if(snapshot.child("imgUrl").getValue()!=null) {
                            if (!snapshot.child("imgUrl").getValue().equals("default")) {
                                imgUrl = snapshot.child("imgUrl").getValue().toString();
                            }
                        //}
                        Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), imgUrl);
                        rowitems.add(item);
                        cardsAdapter.notifyDataSetChanged();
                    }}
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setupNavigation() {
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
}