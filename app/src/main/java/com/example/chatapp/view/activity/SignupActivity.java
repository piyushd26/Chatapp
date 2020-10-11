package com.example.chatapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity implements DataInterface {
    public static final int GALLERYACTIONPICKCODE = 100;
    public static final int My_Permission = 0;
    //private static final Integer[] BACKKGROUNDIMAGES = {R.mipmap.pex1, R.mipmap.pex2, R.mipmap.pex3, R.mipmap.pex4, R.mipmap.pex5, R.mipmap.pex7, R.mipmap.pex8, R.mipmap.pex9};
    Button button_signup;
    EditText Name_et;
    EditText email_et;
    ConstraintLayout layout;
    EditText password_et;
    ImageView male_bat_logo;
    ImageView female_ww_logo;
    LinearLayout linearLayout_batmanMale;
    LinearLayout linearLayout_wwFemale;
    //selected Data
    String selectedGender;
    TextView femaleTV;
    TextView maleTV;
    String selectedPassword;
    SignUpPresenter signUpPresenter;
    String selectedName;
    String selectedEmail;
    CircleImageView profileImage;
    Random random = new Random();
    DataInterface dataInterface;
    BottomSheetDialog bottomSheetDialog;
    ProgressBar progressBar;
    TextView phoneNumber;
    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;
    String selectedPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Utillity.hideNavigationBar(getWindow());
        Utillity.setFullScreen(getWindow());

        button_signup = findViewById(R.id.profile_signup_btn);
        email_et = findViewById(R.id.profile_email);
        password_et = findViewById(R.id.profile_password);
        maleTV = findViewById(R.id.profile_male);
        femaleTV = findViewById(R.id.profile_female);
        male_bat_logo = findViewById(R.id.batman_imageview);
        female_ww_logo = findViewById(R.id.ww_imageview);
        linearLayout_batmanMale = findViewById(R.id.batman_ll);
        linearLayout_wwFemale = findViewById(R.id.ww_ll);
        layout = findViewById(R.id.root_cons_ll);
        Name_et = findViewById(R.id.profile_name);
        //profileImage = findViewById(R.id.profile_picture);
        progressBar = findViewById(R.id.pg_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        phoneNumber = findViewById(R.id.profile_phonenumber);


        BaseFragment.setDataInterface(this);
        signInButton();
        gendermale();
        genderfemale();
        //profileImageSelection();


    }


    private void fireBase() {
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        signUpPresenter.startFetch();
        firebaseAuth.createUserWithEmailAndPassword(selectedEmail, selectedPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    VerificationOfEmail();


                    userID = firebaseAuth.getCurrentUser().getUid();
//FireStore putting values
                    DocumentReference documentReference = firestore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("fName", selectedName);
                    user.put("email", selectedEmail);
                    user.put("phone", selectedPhone);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignupActivity.this, "Firestore data store", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "User Created", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Firestore data not store" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    Toast.makeText(SignupActivity.this, "Error Occurred - " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void VerificationOfEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignupActivity.this, "A verification email has send to this email address", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Verification email isnt send to this address :" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void genderfemale() {
        linearLayout_wwFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_ww_logo.setBackgroundResource(R.drawable.ww_logo_yellow_nobackground_);
                male_bat_logo.setBackgroundResource(R.drawable.batman_logo_white);

                selectedGender = "Male";
            }
        });

    }

    private void gendermale() {
        linearLayout_batmanMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_bat_logo.setBackgroundResource(R.drawable.batman_logoo);
                female_ww_logo.setBackgroundResource(R.drawable.ww_logo_white_new);

                selectedGender = "Female";
            }
        });

    }


    private void profileImageSelection() {
        //layout.setBackgroundResource(random.nextInt(BACKKGROUNDIMAGES.length));
       // profileImage.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
       //         bottomSheetMenu();
       //     }
       // });


    }


    private void bottomSheetMenu() {


        bottomSheetDialog = new BottomSheetDialog(SignupActivity.this, R.style.BottomsheetTheme);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_layout, (ViewGroup) findViewById(R.id.bottom_sheet));

        view.findViewById(R.id.from_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "fromCamera", Toast.LENGTH_LONG).show();

                bottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this, "fromGallery", Toast.LENGTH_LONG).show();

                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();


    }


    private void signInButton() {


        button_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                selectedPassword = password_et.getText().toString().trim();
                selectedName = Name_et.getText().toString().trim();

                if (Name_et.getText().toString().trim().isEmpty() || email_et.getText().toString().isEmpty() || password_et.getText().toString().isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fill Login Details", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //forEmail
                    if (Utillity.validateEmail(email_et)) {

                        selectedEmail = email_et.getText().toString().trim();
                        selectedPhone = phoneNumber.getText().toString().trim();
                        if (!selectedEmail.isEmpty() && !selectedGender.isEmpty() && !selectedName.isEmpty() && !selectedPassword.isEmpty() && !selectedPhone.isEmpty()) {
                            fireBase();

                        } else {
                            Snackbar snackbar = Snackbar.make(layout, "Please Enter All Details", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }


                    } else {
                        Snackbar snackbar = Snackbar.make(layout, "Please Type Valid Email", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }

    }

    public void logintoAccount(View view) {

        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();

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
        Toast.makeText(SignupActivity.this, s, Toast.LENGTH_LONG).show();
    }
}