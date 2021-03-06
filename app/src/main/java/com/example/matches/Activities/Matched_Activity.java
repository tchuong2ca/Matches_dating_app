package com.example.matches.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matches.Adapter.Match_Adapter;
import com.example.matches.Model.Match_Object;
import com.example.matches.Navigation;
import com.example.matches.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Matched_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String currentUid,chatId;
    DatabaseReference dataUser;
    private static final int ACT_NUM = 1;
    private final Context mContext = Matched_Activity.this;
    private RecyclerView.Adapter matchAdap;
    private RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        setupNav();
        currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.match_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        manager= new LinearLayoutManager(Matched_Activity.this);
        recyclerView.setLayoutManager(manager);
        matchAdap = new Match_Adapter(setMatch(),Matched_Activity.this);
        recyclerView.setAdapter(matchAdap);
        getIdmatch();
    }
    private void getIdmatch() {
        DatabaseReference match = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("relative").child("match");
        match.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        getlastmessage(dataSnapshot.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getlastmessage(String key) {

        dataUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("relative").child("match").child(key).child("chatId");
        dataUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatId);
                    Query lastchild= dbref.orderByKey().limitToLast(1);

                    lastchild.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String lastmessage = "", sentby="";
                            if(snapshot.exists()){
                                for(DataSnapshot data : snapshot.getChildren())
                                {
                                    if(data.child("message").getValue()!=null){
                                        lastmessage = data.child("message").getValue().toString();
                                    }

                                    if(data.child("sentby").getValue()!=null){
                                        sentby =data.child("sentby").getValue().toString();
                                    }

                                    fetchInfoMatch(key, lastmessage, sentby);
                                }
                            }
                            else {
                                lastmessage ="No new message";
                                sentby= "";
                                fetchInfoMatch(key, lastmessage, sentby);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void fetchInfoMatch(String key, String lastmessage, String sentby) {
        DatabaseReference infomatchdata = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        infomatchdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String partnerId = snapshot.getKey();
                    String name = "";
                    String des ="";
                    String imgUrl="";
                    if(snapshot.child("name").getValue()!=null){
                        name = snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("proDes").getValue()!=null){
                        des = snapshot.child("proDes").getValue().toString();

                    }
                    if(snapshot.child("imgUrl").getValue()!=null){
                        imgUrl = snapshot.child("imgUrl").getValue().toString();
                    }

                    Match_Object obj = new Match_Object(partnerId, name, des, imgUrl,null,lastmessage,sentby);
                    result.add(obj);
                    matchAdap.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private final ArrayList<Match_Object> result= new ArrayList<Match_Object>();
    private List<Match_Object> setMatch() {
        return result;
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
}
