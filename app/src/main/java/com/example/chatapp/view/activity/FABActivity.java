package com.example.chatapp.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.model.pojo.ChatList;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.adapter.ChatListAdapter;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FABActivity extends AppCompatActivity {

    ChatListAdapter chatListAdapter;
    RecyclerView AllContactsList;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String userID;
    List<ChatList> chatLists = new ArrayList<>();
    StorageReference storageReference;
    SignUpPresenter signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AllContactsList = findViewById(R.id.rv_chatlist);

        chatListAdapter = new ChatListAdapter(this, chatLists,storageReference,signUpPresenter,firebaseUser.getUid(),this);



        setadapter();
        if (firebaseUser != null) {
            Utillity.getcontactsList(firebaseFirestore, userID, chatLists, chatListAdapter, firebaseUser);

        }

    }


    private void setadapter() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        AllContactsList.setLayoutManager(linearLayoutManager);
        AllContactsList.setHasFixedSize(true);
        AllContactsList.setAdapter(chatListAdapter);

    }
}