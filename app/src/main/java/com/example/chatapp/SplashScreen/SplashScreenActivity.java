package com.example.chatapp.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.MainActivity;
import com.example.chatapp.OnBoardingActivity.OnBoardingActivity;
import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    // private static final Integer[] IMAGES = {R.mipmap.pex1, R.mipmap.pex2, R.mipmap.pex3, R.mipmap.pex4, R.mipmap.pex5, R.mipmap.pex6, R.mipmap.pex7, R.mipmap.pex8, R.mipmap.pex9, R.mipmap.pex10};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    FirebaseAuth firebaseAuth;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        Utillity.hideNavigationBar(getWindow());
        Utillity.setFullScreen(getWindow());
        firebaseAuth = FirebaseAuth.getInstance();


        slidesViewPager();
    }

    private void slidesViewPager() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(SplashScreenActivity.this, OnBoardingActivity.class));
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
                /* Create an Intent that will start the Menu-Activity. */

            }
        }, SPLASH_DISPLAY_LENGTH);

    }


}