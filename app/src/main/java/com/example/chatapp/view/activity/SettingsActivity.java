package com.example.chatapp.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.common.Utillity;
import com.example.chatapp.presenter.SignUpPresenter;
import com.example.chatapp.view.DataInterface;
import com.example.chatapp.view.fragments.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements DataInterface {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userId;
    TextView name;
    TextView email;
    FirebaseUser firebaseUser;
    TextView phone;
    TextView status;
    DocumentReference documentReference;
    TextView verified;
    TextView clicktoverify;
    Button buttonreset;
    Button looutFrebaseUser;
    Button resetPassword;
    Button deleteFirebaseUser;
    CircleImageView imageProfile;
    StorageReference storageReference;
    ProgressBar progressBar;
    ImageButton changeProfileImage;
    SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name = findViewById(R.id.name_profile);
        phone = findViewById(R.id.phoneprofile);
        progressBar = findViewById(R.id.pg_settin);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email = findViewById(R.id.emailprofile);
        verified = findViewById(R.id.verify);
        clicktoverify = findViewById(R.id.emailverify);
        firebaseFirestore = FirebaseFirestore.getInstance();
        looutFrebaseUser = findViewById(R.id.logout);
        resetPassword = findViewById(R.id.reset_password);
        deleteFirebaseUser = findViewById(R.id.delete_account);
        imageProfile = findViewById(R.id.image_profile);
        changeProfileImage = findViewById(R.id.select);
        storageReference = FirebaseStorage.getInstance().getReference();
        signUpPresenter=new SignUpPresenter(BaseFragment.getDataInterface());

        //get profile image from Firebase storage
        Utillity.getStoredProfileImage(storageReference,firebaseAuth,signUpPresenter,imageProfile,this);


        checkVerificationOfEmail();
        retreiveFirestoreUserData();


        looutFrebaseUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutProfile();
            }
        });


        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        deleteFirebaseUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFirebaseUser();
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openintent, 1000);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();

                //imageProfile.setImageURI(uri);
                uploadimagetoFirebase(uri);
            }

        }
    }

    private void uploadimagetoFirebase(Uri uri) {
        signUpPresenter = new SignUpPresenter(BaseFragment.getDataInterface());
        signUpPresenter.startFetch();
        final StorageReference reference = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SettingsActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      Picasso.with(SettingsActivity.this).load(uri).into(imageProfile);
                    }
                });
                signUpPresenter.endFetch();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Image not uploaded  ", Toast.LENGTH_LONG).show();
                signUpPresenter.endFetch();
            }
        });
    }

    private void logoutProfile() {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));



    }

    private void checkVerificationOfEmail() {
        if (!firebaseUser.isEmailVerified()) {
            verified.setVisibility(View.VISIBLE);
            clicktoverify.setVisibility(View.VISIBLE);
            clicktoverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SettingsActivity.this, "A verification email has send to this email address", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsActivity.this, "Verification email isnt send to this address :" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });
        }
    }

    private void retreiveFirestoreUserData() {
        userId = FirebaseAuth.getInstance().getUid();
        documentReference = firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));
            }
        });
    }


    public void deleteFirebaseUser() {
        FirebaseAuth.getInstance().getCurrentUser().delete();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));

    }

    public void resetPassword() {
        final EditText resetPassword = new EditText(this);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Reset Password");
        alertDialog.setMessage("Enter New Password > 8 Characters long");
        alertDialog.setView(resetPassword);


        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newPassword = resetPassword.getText().toString();
                firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Password updated", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(SettingsActivity.this, "Password not updated", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create().show();
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