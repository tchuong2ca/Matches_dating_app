package com.example.matches.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matches.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.ChatViewHolder> {
    private List<Chat_Object> chat_objects;
    private Context context;


    public Chat_Adapter(List<Chat_Object>match_objects, Context context){
        this.chat_objects= match_objects;
        this.context=context;
    }
    @NonNull
    @Override
    public Chat_Adapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_left,parent,false);
        return new Chat_Adapter.ChatViewHolder(layoutview);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Adapter.ChatViewHolder holder,
                                 int position) {
        final Chat_Object chat_object = chat_objects.get(position);
        holder.message.setText(chat_object.getMessage());
if(chat_objects.get(position).getCurrentuser()){
    Picasso.get().load(chat_object.getPartnerAvt()).into(holder.partneravatar);
    holder.container.setGravity(Gravity.END);
    holder.message.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
    holder.message.setBackgroundResource(R.drawable.send_bg);
    holder.message.setTextColor(Color.parseColor("#FFFFFF"));
}
else {
    Picasso.get().load(chat_object.getPartnerAvt()).into(holder.partneravatar);
    holder.message.setTextColor(Color.parseColor("#000000"));
}
    }
    @Override
    public int getItemCount() {
        return chat_objects.size();
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView message;
        public LinearLayout container;
public CircleImageView partneravatar;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            message = itemView.findViewById(R.id.msg);
            container = itemView.findViewById(R.id.container);
            partneravatar = itemView.findViewById(R.id.pt_ava);
        }
        @Override
        public void onClick(View view){

        }

    }

}
