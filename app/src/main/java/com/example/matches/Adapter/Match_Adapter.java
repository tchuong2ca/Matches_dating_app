package com.example.matches.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matches.Activities.Chat_Activity;
import com.example.matches.Model.Match_Object;
import com.example.matches.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Match_Adapter extends RecyclerView.Adapter<Match_Adapter.MyViewHolder> {
    private List<Match_Object> match_objects;
    String currentUid;


    public Match_Adapter(List<Match_Object>match_objects, Context context){
        this.match_objects= match_objects;
    }
    @NonNull
    @Override
    public Match_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item,parent,false);
        currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new MyViewHolder(layoutview);
    }

    @Override
    public void onBindViewHolder(@NonNull Match_Adapter.MyViewHolder holder,
                                 int position) {
        Match_Object match_object = match_objects.get(position);
        holder.name.setText(match_object.getName());
        if (match_object.getSentby().equals(match_object.getPartnerId())){
            holder.des.setText(match_object.getName()+": "+match_object.getLastmessage());
        }
        else if (match_object.getSentby().equals("")){
            holder.des.setText(match_object.getLastmessage());
        }
        else {
            holder.des.setText("You: "+ match_object.getLastmessage());
        }
        holder.partnerId.setText(match_object.getPartnerId());
        if(match_object.getImgUrl()!=null){
            Picasso.get().load(match_object.getImgUrl()).into(holder.imageView);
        }
    }
    @Override
    public int getItemCount() {
        return match_objects.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        CircleImageView imageView;
        TextView name, des, partnerId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            des = itemView.findViewById(R.id.des);
            partnerId= itemView.findViewById(R.id.partnerId);
        }
        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), Chat_Activity.class);
            Bundle bundle= new Bundle();
            bundle.putString("partnerId",partnerId.getText().toString());
            bundle.putString("partnerName",name.getText().toString());

            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        }
    }
}
