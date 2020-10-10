package com.example.chatapp.OnBoardingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.view.activity.LoginActivity;
import com.example.chatapp.view.activity.SignupActivity;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {
   // private static final Integer[] IMAGES = {R.mipmap.pex1, R.mipmap.pex2, R.mipmap.pex3, R.mipmap.pex4, R.mipmap.pex5, R.mipmap.pex6, R.mipmap.pex7, R.mipmap.pex8, R.mipmap.pex9, R.mipmap.pex10};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    onBoardingAdapter onBoardingAdapter;
    LinearLayout linearLayoutIndicator;
    Button btn_next;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_pager);
        BaseFragment.setOnBoardingActivity(this);
        Utillity.hideNavigationBar(getWindow());
        Utillity.setFullScreen(getWindow());

        btn_next = findViewById(R.id.btn_next);
        linearLayoutIndicator = findViewById(R.id.indicators_layout);


        setupOnBoardingItems();

        final ViewPager2 viewPager2 = findViewById(R.id.pager);
        viewPager2.setAdapter(onBoardingAdapter);

        setupOnBoardingIndicators();
        setCurrentOnboardingIndicators(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager2.getCurrentItem() + 1 < onBoardingAdapter.getItemCount()) {

                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);


                } else {

                    if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                    {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                        finish();
                    }



                }
            }
        });
    }

    public void setupOnBoardingItems() {

        List<onBoardingItem> onBoardingItemsList = new ArrayList<>();

        onBoardingItem onBoardingItem = new onBoardingItem();
        onBoardingItem.setTitle("Chat With Friends");
        onBoardingItem.setDescription("");
        onBoardingItem.setImage(R.mipmap.pex1);
        //https://tenor.com/view/error-wait-computer-download-cat-gif-10480536 ERRRROORRR
        onBoardingItem item = new onBoardingItem();
        item.setTitle("Make Video Calls");
        item.setDescription("");
        item.setImage(R.mipmap.pex2);

        onBoardingItem boardingItem = new onBoardingItem();
        boardingItem.setTitle("Hangout!");
        boardingItem.setDescription("");
        boardingItem.setImage(R.mipmap.pex3);

        onBoardingItemsList.add(onBoardingItem);
        onBoardingItemsList.add(item);
        onBoardingItemsList.add(boardingItem);

        onBoardingAdapter = new onBoardingAdapter(OnBoardingActivity.this,onBoardingItemsList);

    }

    private void setupOnBoardingIndicators() {
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            indicators[i].setLayoutParams(layoutParams);
            linearLayoutIndicator.addView(indicators[i]);


        }
    }

    private void setCurrentOnboardingIndicators(int index) {
        int childcount = linearLayoutIndicator.getChildCount();
        for (int i = 0; i < childcount; i++) {
            ImageView imageView = (ImageView) linearLayoutIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));

            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
        if (index == onBoardingAdapter.getItemCount() - 1) {

            btn_next.setText("Start");

        } else {

            btn_next.setText("Next");

        }
    }


}