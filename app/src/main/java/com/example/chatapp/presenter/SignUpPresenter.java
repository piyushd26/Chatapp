package com.example.chatapp.presenter;

import android.content.Context;

import com.example.chatapp.view.DataInterface;

public class SignUpPresenter {


    DataInterface data_interface;

    public SignUpPresenter(DataInterface dataInterface) {
        this.data_interface = dataInterface;

    }

    public void startFetch() {
        data_interface.onFetchStart();
    }

    public void endFetch() {
        data_interface.onFetchComplete();
    }



}
