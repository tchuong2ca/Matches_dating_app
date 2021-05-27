package com.example.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Match_Adapter extends RecyclerView.Adapter<Match_Adapter.MyViewHolder> {
private List<Match_Object> match_objects;
public Match_Adapter(List<Match_Object>match_objects, Context context){
    this.match_objects= match_objects;
}
    @NonNull
    @Override
    public Match_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item,parent,false);
    return new MyViewHolder(layoutview);
    }

    @Override
    public void onBindViewHolder(@NonNull Match_Adapter.MyViewHolder holder,
                                 int position) {
Match_Object match_object = match_objects.get(position);
holder.name.setText(match_object.getName());
holder.des.setText(match_object.getDes());
if(match_object.getImgUrl()!=null){
    Picasso.get().load(match_object.getImgUrl()).into(holder.imageView);
}
    }

    @Override
    public int getItemCount() {
        return match_objects.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView imageView;
        TextView name, des;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            des = itemView.findViewById(R.id.des);
        }
        @Override
        public void onClick(View view){

        }

    }

}
