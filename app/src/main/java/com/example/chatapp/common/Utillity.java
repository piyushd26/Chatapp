package com.example.chatapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapp.model.pojo.ChatList;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.activity.SettingsActivity;
import com.example.chatapp.view.adapter.ChatListAdapter;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
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

    public static void getStoredProfileImage(StorageReference storageReference, final SignUpPresenter signUpPresenter, final CircleImageView imageProfile, final Activity activity, String userid) {
        StorageReference profileref = storageReference.child("users/"+userid+"/profile.jpg");
        signUpPresenter.startFetch();
        profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(activity).load(uri).into(imageProfile);
                signUpPresenter.endFetch();
            }
        });
    }

    public static void getcontactsList(final FirebaseFirestore firebaseFirestore, final String userID, final List<ChatList> chatLists, final ChatListAdapter chatListAdapter, final FirebaseUser firebaseUser) {
            firebaseFirestore.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String username = snapshot.getString("fName");
                        String userid =snapshot.getString("userid");
                        String userprofile =snapshot.getString("userprofile");

                        ChatList chatList = new ChatList();
                        chatList.setUsername(username);
                        chatList.setUserid(userid);
                        chatList.setUrlProfile(userprofile);




                        if (userID != null && userID.equals(firebaseUser.getUid())) {
                            chatLists.add(chatList);

                        }
                        chatListAdapter.updateData(chatLists,userID);
                    }

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
