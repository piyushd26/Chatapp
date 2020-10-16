package com.example.chatapp.view.adapter;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.pojo.NewChat;

import java.util.ArrayList;
import java.util.List;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.ViewHolder> {

    List<NewChat> newChats = new ArrayList<>();
    Context ctx;
    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    public NewChatAdapter (Context context,List<NewChat> newChats)
    {
        this.ctx=context;
        this.newChats=newChats;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_RIGHT)
        {
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_left,parent,false);
            return new ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.chat_item_right,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(newChats.get(position));
    }

    @Override
    public int getItemCount() {
        return newChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_text_message);
        }
        void bind(NewChat newChat)
        {
            textView.setText(newChat.getTextMessage());
        }
    }
}
