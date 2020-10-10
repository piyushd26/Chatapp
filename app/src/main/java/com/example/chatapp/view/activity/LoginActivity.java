package com.example.chatapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.DataInterface;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements DataInterface {

    FirebaseAuth firebaseAuth;
    Button buttonLogin;
    EditText emailET;
    EditText passwordET;
    ConstraintLayout layout;
    TextView forgotPassword;
    SignUpPresenter signUpPresenter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utillity.hideNavigationBar(getWindow());
        Utillity.setFullScreen(getWindow());
        BaseFragment.setDataInterface(this);

        emailET = findViewById(R.id.profile_email_login);
        passwordET = findViewById(R.id.profile_password_login);
        buttonLogin = findViewById(R.id.profile_login_btn);
        layout = findViewById(R.id.root_login_constraintlayout);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgot_password);
        progressBar=findViewById(R.id.pg_login_);

        loginButton();
    }

    private void loginButton() {
       signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpPresenter.startFetch();
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if (Utillity.validateEmail(emailET) && Utillity.validatePassword(passwordET)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "User Loged In", Toast.LENGTH_LONG).show();
                            } else {

                                Snackbar snackbar = Snackbar.make(layout, "Invalid Email or Password", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            signUpPresenter.endFetch();
                        }
                    });
                } else {
                    signUpPresenter.endFetch();
                    Toast.makeText(LoginActivity.this, "Login details are'nt correct", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void SignintoAccount(View view) {

        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void forgotPassword(View view) {

        startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));

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