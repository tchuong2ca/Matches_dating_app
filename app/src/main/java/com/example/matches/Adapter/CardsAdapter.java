package com.example.matches.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.matches.Activity.Check_info;
import com.example.matches.Model.Users;
import com.example.matches.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardsAdapter extends ArrayAdapter<Users> {
    Context mContext;
    public CardsAdapter(@NonNull Context context, int resource, List<Users> list) {
        super(context, resource, list);
        this.mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Users users = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);
        }
        TextView name= (TextView) convertView.findViewById(R.id.name);
        ImageView image= (ImageView) convertView.findViewById(R.id.image);
        ImageButton btnInfo = convertView.findViewById(R.id.checkinfo);
        TextView age =(TextView) convertView.findViewById(R.id.age);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Check_info.class);
                intent.putExtra("name", users.getName() );
                intent.putExtra("photo", users.getImgUrl());
                intent.putExtra("age", users.getAge());
                intent.putExtra("description", users.getDescription());
                intent.putExtra("address", users.getAddress());
                intent.putExtra("contact", users.getContact());
                intent.putExtra("hobbies", users.getHobbies());
                mContext.startActivity(intent);
            }
        });

        name.setText(users.getName());
        age.setText(users.getAge());
        if ("default".equals(users.getImgUrl())) {
            Picasso.get().load(R.drawable.userlogo).into(image);
        } else {
            Picasso.get().load(users.getImgUrl()).into(image);
        }
        return convertView;
    }
}
