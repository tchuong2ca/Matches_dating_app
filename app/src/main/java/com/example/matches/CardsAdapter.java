package com.example.matches;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardsAdapter extends ArrayAdapter<Cards> {
    Context mContext;
    public CardsAdapter(@NonNull Context context, int resource, List<Cards> list) {
        super(context, resource, list);
        this.mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cards cards = getItem(position);

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
                intent.putExtra("name", cards.getName() );
                intent.putExtra("photo", cards.getImgUrl());
                intent.putExtra("age",cards.getAge());
                intent.putExtra("description", cards.getDescription());
                intent.putExtra("address", cards.getAddress());
                intent.putExtra("contact", cards.getContact());
                intent.putExtra("hobbies", cards.getHobbies());
                mContext.startActivity(intent);
            }
        });

        name.setText(cards.getName());
        age.setText(cards.getAge());
        if ("default".equals(cards.getImgUrl())) {
            Picasso.get().load(R.drawable.userlogo).into(image);
        } else {
            Picasso.get().load(cards.getImgUrl()).into(image);
        }
        return convertView;
    }
}
