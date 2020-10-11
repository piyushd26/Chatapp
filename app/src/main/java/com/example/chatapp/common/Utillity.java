package com.example.chatapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.activity.SettingsActivity;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utillity {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern;
    private static Matcher matcher;


    public static void hideNavigationBar(Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void storeIntegerSF(Context context, int s) {
        SharedPreferences sharedPreferences = BaseFragment.getsharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("activitypager", s);
        editor.putInt("onboardingactivity", s);
        editor.apply();

    }

    public static int getIntSF(Context context) {
        int res = 0;
        SharedPreferences sharedPreferences = BaseFragment.getsharedPreferences();
        int activitypager = sharedPreferences.getInt("activitypager", 0);
        int OnBoardingActivity = sharedPreferences.getInt("onboardingactivity", 0);
        if (context == BaseFragment.getMainActivity()) {
            res = activitypager;
        } else if (context == BaseFragment.getOnBoardingActivity()) {
            res = OnBoardingActivity;
        }

        return res;
    }


    public static void hideSystemUI(Window window) {

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE

                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void setFullScreen(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = window;
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static boolean validateEmail(EditText email_et) {


            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email_et.getText().toString());

            if(!matcher.matches())
                email_et.setError("Enter valid email");

            return matcher.matches();



    }

    public static boolean validatePassword(EditText passwordET) {
        if(passwordET.length() < 8)
        {
           passwordET.setError("8 characters minimus");

            return false;
        }
        else {
            return true;
        }

    }

    public static void getStoredProfileImage(StorageReference storageReference, FirebaseAuth firebaseAuth, final SignUpPresenter signUpPresenter, final CircleImageView imageProfile, final SettingsActivity settingsActivity) {
        StorageReference profileref = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        signUpPresenter.startFetch();
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(settingsActivity).load(uri).into(imageProfile);
                signUpPresenter.endFetch();
            }
        });
    }


    private void showSystemUI(Window window) {
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
