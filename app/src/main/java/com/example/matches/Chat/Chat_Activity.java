package com.example.matches.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.matches.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Chat_Activity extends AppCompatActivity {

    private String currentUid,partnerId,name, avaUrl,chatId, getAvt;
    private RecyclerView recyclerView;

    FirebaseAuth auth;
    DatabaseReference databaseReference,dataUser, dataChat,currentAvt;
    private RecyclerView.Adapter chatAdap;
    TextView partnerName;
    ImageButton back;
    ImageView avt;
    private EditText Message;
    private ImageButton sendbtn;
    private RecyclerView.LayoutManager Chatmanager;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat);

        auth= FirebaseAuth.getInstance();
        partnerId = getIntent().getExtras().getString("partnerId");
        currentUid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        dataUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("relative").child("match").child(partnerId).child("chatId");
        dataChat = FirebaseDatabase.getInstance().getReference().child("Chat");
currentAvt = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("imgUrl");

        name = getIntent().getExtras().getString("partnerName");
        getChatId();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        Chatmanager= new LinearLayoutManager(Chat_Activity.this);
        recyclerView.setLayoutManager(Chatmanager);
        chatAdap = new Chat_Adapter(getDataSetChat(),Chat_Activity.this);
        recyclerView.setAdapter(chatAdap);


        avt = findViewById(R.id.partner_avt);
        Message= findViewById(R.id.message);
        sendbtn= findViewById(R.id.send);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        partnerName = findViewById(R.id.partnerName);
        partnerName.setText(name);

        back = findViewById(R.id.back_chat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getimg();
    }

    private void sendMessage() {
currentAvt.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {



        String sendMsg = Message.getText().toString();
        if(!sendMsg.isEmpty()){
            DatabaseReference newMsgDb = dataChat.push();
            Map newMsg = new HashMap();
            newMsg.put("sentby",currentUid);
            newMsg.put("message",sendMsg);
            newMsg.put("avatar",snapshot.getValue());
            newMsgDb.setValue(newMsg);
        }
        Message.setText(null);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});



    }
    private void getChatId(){
        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    dataChat = dataChat.child(chatId);
                    getMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessages() {

        dataChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message  = null;
                    String sentby = null;
                    String avatar  =null;
                    if(snapshot.child("avatar").getValue()!=null){
                        avatar = snapshot.child("avatar").getValue().toString();
                    }
                    if(snapshot.child("message").getValue()!=null){
                        message = snapshot.child("message").getValue().toString();
                    }
                    if(snapshot.child("sentby").getValue()!=null){
                        sentby = snapshot.child("sentby").getValue().toString();
                    }

                    if(message!=null&&sentby!=null&&avatar!=null){
                        Boolean boolenCurUsr = false;
                        if(sentby.equals(currentUid)){
                            boolenCurUsr = true;
                        }
                        Chat_Object newmessage = new Chat_Object(message,boolenCurUsr,avatar);
                        result.add(newmessage);
                        chatAdap.notifyDataSetChanged();
                    }

                }
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

    private void getimg() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(partnerId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>)snapshot.getValue();
                    if(map.get("imgUrl")!=null){
                        avaUrl = map.get("imgUrl").toString();
                        switch(avaUrl){
                            case "default":
                                Picasso.get().load(R.drawable.userlogo).into(avt);
                                break;
                            default:
                                Picasso.get().load(avaUrl).into(avt);
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


    private final ArrayList<Chat_Object> result= new ArrayList<Chat_Object>();
    private List<Chat_Object> getDataSetChat() {
        return result;
    }
}

