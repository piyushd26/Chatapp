package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.OnBoardingActivity.OnBoardingActivity;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.model.pojo.UserData;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.DataInterface;
import com.example.chatapp.view.activity.SettingsActivity;
import com.example.chatapp.view.adapter.ViewPagerAdapter;
import com.example.chatapp.view.fragments.BaseFragment;
import com.example.chatapp.view.fragments.Tab1;
import com.example.chatapp.view.fragments.Tab2;
import com.example.chatapp.view.fragments.Tab3;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
    TextView toolbarTV;
    UserData userData;
    Bundle bundle = new Bundle();
    DocumentReference documentReference;
    Toolbar toolbar;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseFragment.setMainActivity(this);
        BaseFragment.setDataInterface(this);

        Utillity.hideSystemUI(getWindow());
        Utillity.hideNavigationBar(getWindow());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        userData = new UserData();
        toolbarTV = findViewById(R.id.toolbar_tv);

        tabLayout = findViewById(R.id.tablayput);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.include_toolbar);


        setSupportActionBar(toolbar);
        toolbarTV.setText("Konnect");

      //  retrievedDataFromFireStore();

        viewPager();

    }

    private void viewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(Tab1.getInstance(), "Chats");
        viewPagerAdapter.addFragment(Tab2.getInstance(), "Status");
        viewPagerAdapter.addFragment(Tab3.getInstance(), "Calls");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void retrievedDataFromFireStore() {

        documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                name = documentSnapshot.getString("fName");
                email = documentSnapshot.getString("email");
                phone = documentSnapshot.getString("phone");


            }
        });

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