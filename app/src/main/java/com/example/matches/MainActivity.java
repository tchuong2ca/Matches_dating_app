package com.example.matches;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CardsAdapter cardsAdapter;
    private DatabaseReference userDb;
    private FirebaseAuth auth;
    private final Context mContext = MainActivity.this;
    private static final int ACT_NUM = 0;
    private String curentId, imgUrl;

    FrameLayout cardFrame;

    List<Cards> rowitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        cardFrame = findViewById(R.id.card_frame);


        auth=FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        curentId = auth.getCurrentUser().getUid();

        setupNavigation();

        checkgender();


        rowitems = new ArrayList<Cards>();
        cardsAdapter = new CardsAdapter(this, R.layout.item, rowitems );


        swipetoexpress();
    }
    private void swipetoexpress() {
        SwipeFlingAdapterView flingAdapterView = findViewById(R.id.frame);

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

            }
            @Override
            public void onRightCardExit(Object dataObject) {
                Cards cards= (Cards) dataObject;
                String uid = cards.getUserId();
                userDb.child(uid).child("relative").child("like").child(curentId).setValue("true");
                status(uid);

            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingAdapterView.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
        flingAdapterView.setOnItemClickListener((itemPosition, dataObject) -> Toast.makeText(MainActivity.this, "Clicked!",Toast.LENGTH_SHORT).show());
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
                        if ("male".equals(gender)) {
                            oppositegender = "female";
                        } else if ("female".equals(gender)) {
                            oppositegender = "male";
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
                        String age = "0";
                        String contact = "not set yet";
                        String hobbies="not set yet";
                        String description ="not set yet";
                        String address = "not set yet";

                            if (!snapshot.child("imgUrl").getValue().equals("default")
                                    && !snapshot.child("proAge").getValue().equals("0")
                                    && !snapshot.child("proContact").getValue().equals("not set yet")
                                    && !snapshot.child("proHob").getValue().equals("not set yet")
                                    && !snapshot.child("proDes").getValue().equals("not set yet")
                                    && !snapshot.child("proAdd").getValue().equals("not set yet")) {


                                imgUrl = snapshot.child("imgUrl").getValue().toString();
                                age = snapshot.child("proAge").getValue().toString();
                                contact = snapshot.child("proContact").getValue().toString();
                                hobbies = snapshot.child("proHob").getValue().toString();
                                description = snapshot.child("proDes").getValue().toString();
                                address = snapshot.child("proAdd").getValue().toString();
                            }

                        Cards item = new Cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), imgUrl,age,address,hobbies,contact,description);
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