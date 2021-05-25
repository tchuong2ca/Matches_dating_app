package com.example.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardsAdapter extends ArrayAdapter<Cards> {
    public CardsAdapter(@NonNull Context context, int resource, List<Cards> list) {
        super(context, resource, list);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cards cards = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent,false);
        }
        TextView name= (TextView) convertView.findViewById(R.id.name);
        ImageView image= (ImageView) convertView.findViewById(R.id.image);

        name.setText(cards.getName());
        switch (cards.getImgUrl()){

            case "default":
                Glide.with(getContext()).load(R.drawable.userlogo).into(image);
                break;
            default:
                Glide.with(getContext()).clear(image);
                Glide.with(getContext()).load(cards.getImgUrl()).into(image);
                break;


        }


        return convertView;
    }
}
