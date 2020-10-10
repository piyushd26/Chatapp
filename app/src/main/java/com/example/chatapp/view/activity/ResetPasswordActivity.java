package com.example.chatapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.DataInterface;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements DataInterface {

    EditText enterEmail;
    SignUpPresenter signUpPresenter;
    FirebaseAuth firebaseAuth;
    Button resetbutton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Utillity.hideNavigationBar(getWindow());
        Utillity.setFullScreen(getWindow());

        enterEmail = findViewById(R.id.resetpassword);
        resetbutton = findViewById(R.id.reserbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.pg_reset_);

        enterEmailButton();


    }

    private void enterEmailButton() {
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpPresenter.startFetch();
                final String email = enterEmail.getText().toString().trim();
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ResetPasswordActivity.this, "Email has send to this accout", Toast.LENGTH_LONG).show();
                        signUpPresenter.endFetch();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, "Error! Try again later" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchComplete() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFetchFailure(String s) {
        progressBar.setVisibility(View.GONE);
    }
}