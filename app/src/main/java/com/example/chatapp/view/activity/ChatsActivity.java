package com.example.chatapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {


    String username;
    String userid;
    String userProfile;
    ImageButton back;
    CircleImageView circleImageView;
    TextView chatprofilename;
    TextView chatlastseen;
    ImageButton sendBtn;
    EditText edmessage;
    FirebaseUser firebaseUser;
    String receiverId;
    StorageReference storageReference;
    SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        receiverId = intent.getStringExtra("userID");
        sendBtn = findViewById(R.id.sendbtn);
        userid = FirebaseAuth.getInstance().getUid();
        chatprofilename = findViewById(R.id.namechat);
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        back = findViewById(R.id.btn_back);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userProfile = intent.getStringExtra("userProfile");
        circleImageView = findViewById(R.id.chat_image);
        chatlastseen = findViewById(R.id.seen);
        edmessage = findViewById(R.id.Type);


        Utillity.getStoredProfileImage(storageReference, signUpPresenter, circleImageView, this, userid);

        if (receiverId != null) {
            Picasso.with(ChatsActivity.this).load(userProfile).into(circleImageView);
            chatprofilename.setText(username);

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = edmessage.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), receiverId, msg);
                } else {
                    Toast.makeText(ChatsActivity.this, "Please send a non empty messege", Toast.LENGTH_LONG).show();
                }
                edmessage.setText("");


            }
        });


    }

    private void sendMessage(String sender, String receiver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);


    }

}