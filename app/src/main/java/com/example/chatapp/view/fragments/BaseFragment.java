package com.example.chatapp.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

import com.example.chatapp.MainActivity;
import com.example.chatapp.OnBoardingActivity.OnBoardingActivity;
import com.example.chatapp.common.Constants;
import com.example.chatapp.view.DataInterface;

public class BaseFragment extends Fragment {

   static MainActivity mainActivity ;
   static OnBoardingActivity onBoardingActivity;
   static DataInterface dataInterface;
   static Context profileFragment;

    public static Context getProfileFragment() {
        return profileFragment;
    }

    public static void setProfileFragment(Context profileFragment) {
        BaseFragment.profileFragment = profileFragment;
    }

    public static DataInterface getDataInterface() {
        return dataInterface;
    }

    public static void setDataInterface(DataInterface dataInterface) {
        BaseFragment.dataInterface = dataInterface;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        BaseFragment.mainActivity = mainActivity;
    }

    public static OnBoardingActivity getOnBoardingActivity() {
        return onBoardingActivity;
    }

    public static void setOnBoardingActivity(OnBoardingActivity onBoardingActivity) {
        BaseFragment.onBoardingActivity = onBoardingActivity;
    }

    public static SharedPreferences getsharedPreferences(){
        return mainActivity.getSharedPreferences(Constants.SF, Context.MODE_PRIVATE);
    }

}