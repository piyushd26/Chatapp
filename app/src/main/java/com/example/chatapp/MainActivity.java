package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.OnBoardingActivity.OnBoardingActivity;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.model.pojo.ChatList;
import com.example.chatapp.model.pojo.UserData;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.DataInterface;
import com.example.chatapp.view.activity.FABActivity;
import com.example.chatapp.view.activity.SettingsActivity;
import com.example.chatapp.view.adapter.ChatListAdapter;
import com.example.chatapp.view.adapter.ViewPagerAdapter;
import com.example.chatapp.view.fragments.BaseFragment;
import com.example.chatapp.view.fragments.Tab1;
import com.example.chatapp.view.fragments.Tab2;
import com.example.chatapp.view.fragments.Tab3;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements DataInterface {


    BottomNavigationView navigationView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    SignUpPresenter signUpPresenter;
    TabLayout tabLayout;
    ViewPager viewPager;
    String name;
    String email;
    String phone;
    FloatingActionButton floatingActionButton;
    ChatListAdapter chatListAdapter;
    TextView toolbarTV;
    UserData userData;
    List<ChatList> chatLists = new ArrayList<>();
    Bundle bundle = new Bundle();
    RecyclerView chatListRV;
    ChatList chatList = new ChatList();
    DocumentReference documentReference;
    Toolbar toolbar;
    FirebaseUser firebaseUser;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseFragment.setMainActivity(this);
        BaseFragment.setDataInterface(this);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        userData = new UserData();
        toolbarTV = findViewById(R.id.toolbar_tv);
        chatListRV = findViewById(R.id.rv_chatlist);
        floatingActionButton= findViewById(R.id.fab);

        tabLayout = findViewById(R.id.tablayput);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.include_toolbar);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);

        //  retrievedDataFromFireStore();

        viewPager();





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, FABActivity.class));
            }
        });
    }










    private void viewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(Tab1.getInstance(), "Chats");
        viewPagerAdapter.addFragment(Tab2.getInstance(), "Status");
        viewPagerAdapter.addFragment(Tab3.getInstance(), "Calls");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }



    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
            //activityPager = 1;

        }


    }


    @Override
    public void LoginDeatils() {

    }

    @Override
    public void SignUpDetails() {

    }

    @Override
    public void MainActivity() {

    }

    @Override
    public void ResetPasswordActivity() {

    }

    @Override
    public void onFetchStart() {

    }

    @Override
    public void onFetchComplete() {

    }

    @Override
    public void onFetchFailure(String s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.bnv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.new_group) {


            return true;
        } else if (itemId == R.id.starred_messages) {

            return true;
        } else if (itemId == R.id.navigation_profile) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}