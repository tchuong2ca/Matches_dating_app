package com.example.matches.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matches.Adapter.CardsAdapter;
import com.example.matches.Model.Match_Object;
import com.example.matches.Model.Users;
import com.example.matches.Navigation;
import com.example.matches.Notification.SendNotification;
import com.example.matches.R;
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
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardsAdapter cardsAdapter;
    private DatabaseReference userDb;
    private FirebaseAuth auth;
    private final Context mContext = MainActivity.this;
    private static final int ACT_NUM = 0;
    private String curentId;
    ImageButton like, nope;
    FrameLayout cardFrame;

    List<Users> rowitems;
    private static final String ONESIGNAL_APP_ID = "3a44c285-3b66-4e55-a778-0f7230e57688";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OSDeviceState deviceState = OneSignal.getDeviceState();
        String userId = deviceState != null ? deviceState.getUserId() : null;
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);


        new SendNotification("message 1", "heading 1", null);


        cardFrame = findViewById(R.id.card_frame);
        auth=FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        curentId = auth.getCurrentUser().getUid();
        setupNavigation();

        checkgender();
        like= findViewById(R.id.likebtn);
        nope = findViewById(R.id.dislikebtn);

        rowitems = new ArrayList<Users>();
        cardsAdapter = new CardsAdapter(this, R.layout.item, rowitems );


        swipetoexpress();
    }
    public void swipetoexpress() {
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
                Users users = (Users) dataObject;
                String uid = users.getUserId();
                userDb.child(uid).child("relative").child("nope").child(curentId).setValue("true");
                userDb.child(uid).child("relative").child("nope").child(curentId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userDb.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(MainActivity.this, "you disliked "+snapshot.child("name").getValue().toString(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onRightCardExit(Object dataObject) {
                Users users = (Users) dataObject;
                String otherUserId = users.getUserId();

                userDb.child(otherUserId).child("relative").child("like").child(curentId).setValue("true");
                status(otherUserId);

                userDb.child(otherUserId).child("relative").child("like").child(curentId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userDb.child(otherUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Toast.makeText(MainActivity.this, "you liked "+snapshot.child("name").getValue().toString(),Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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
    String notificationKey;
    public void status(String otherUserid) {
        DatabaseReference statusDb = userDb.child(curentId).child("relative").child("like").child(otherUserid);
        statusDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast toast= Toast.makeText(MainActivity.this,"It's a Match !!! <3",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    String matchPartnerId = snapshot.getKey();
                    //thêm id của new partner vào child chatid của mình
                    userDb.child(matchPartnerId).child("relative").child("match").child(curentId).child("chatId").setValue(key);
                    //thêm id của mình vào chatId của new partner
                    userDb.child(curentId).child("relative").child("match").child(matchPartnerId).child("chatId").setValue(key);
                    String message="Heyy, you have a new match !!!";
                    getNotificationKey(new MainActivity.MyCallback() {
                        @Override
                        public void onCallback(String notificationKey, String name) {
                            if(!matchPartnerId.equals(FirebaseAuth.getInstance().getUid())){
                                new SendNotification(message, "New match with "+name, notificationKey);
                            }
                        }
                    }, matchPartnerId);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    String name;
    public interface MyCallback{
        void onCallback(String notificationKey, String name);
    }

    private void getNotificationKey(final MainActivity.MyCallback myCallback, String matchPartnerId){

        DatabaseReference sender = FirebaseDatabase.getInstance().getReference().child("Users").child(curentId).child("name");
        sender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference mNotificationKey = FirebaseDatabase.getInstance().getReference().child("Users").child(matchPartnerId).child("notificationKey");
        mNotificationKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    notificationKey = dataSnapshot.getValue().toString();
                    Match_Object mUser = new Match_Object(matchPartnerId,name,null,null,notificationKey,null,null);
                    mUser.setNotificationKey(notificationKey);
                    myCallback.onCallback(mUser.getNotificationKey(), name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    if (snapshot.exists()
                            //kiểm tra currentid có trong child "nope" của người khác
                            && !snapshot.child("relative").child("nope").hasChild(curentId)

                            //kiểm tra currentid có trong child "like" của người khác
                            && !snapshot.child("relative").child("like").hasChild(curentId)

                            //truy vấn những thành viên có giới tính trái ngược
                            &&snapshot.child("gender").getValue().toString().equals(oppositegender)) {
                        //thì
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

                        Users item = new Users(snapshot.getKey(), snapshot.child("name").getValue().toString(), imgUrl,age,address,hobbies,contact,description);
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