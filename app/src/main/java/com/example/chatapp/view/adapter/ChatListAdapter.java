package com.example.chatapp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.model.pojo.ChatList;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.activity.ChatsActivity;
import com.example.chatapp.view.activity.FABActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    FABActivity fabActivity;
    Context ctx;
    List<ChatList> chatLists = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    StorageReference storageReference;
    SignUpPresenter signUpPresenter ;
    String firebaseUserid;



    public ChatListAdapter(Context context, List<ChatList> chatList, StorageReference storageReference, SignUpPresenter signUpPresenter, String firebaseUser, FABActivity fabActivity) {
        this.ctx = context;
        this.chatLists = chatList;
        this.firebaseUserid=firebaseUser;
        this.signUpPresenter=signUpPresenter;
        this.storageReference=storageReference;
        this.fabActivity=fabActivity;

    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_chatlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ViewHolder holder, int position) {
        final ChatList chatList = chatLists.get(position);
        holder.name.setText(chatList.getUsername());
        holder.des.setText(chatList.getDescription());
        holder.date.setText(chatList.getDate());
        if (chatList.getUrlProfile() == null) {
            Picasso.with(ctx).load(R.mipmap.applogo_logotype).into(holder.circleImageView);
        } else {
            Picasso.with(ctx).load(chatList.getUrlProfile()).into(holder.circleImageView);
        }


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ChatsActivity.class);
                intent.putExtra("userID", chatList.getUserid());
                intent.putExtra("username", chatList.getUsername());
                intent.putExtra("userProfile", chatList.getUrlProfile());
                ctx.startActivity(intent);
            }
        });

        Utillity.getStoredProfileImage(storageReference,signUpPresenter,holder.circleImageView,fabActivity,firebaseUserid);

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateData(List<ChatList> chatList) {
        this.chatLists = chatList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView des;
        CircleImageView circleImageView;
        TextView date;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.root_chatactivity);
            name = itemView.findViewById(R.id.chatlist_name);
            des = itemView.findViewById(R.id.descrip);
            date = itemView.findViewById(R.id.chatlist_date);
            circleImageView = itemView.findViewById(R.id.chatlist_image);
        }
    }
}
