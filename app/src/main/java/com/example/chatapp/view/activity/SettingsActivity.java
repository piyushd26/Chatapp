package com.example.chatapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
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

import javax.annotation.Nullable;

public class SettingsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name = findViewById(R.id.name_profile);
        phone = findViewById(R.id.phoneprofile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email = findViewById(R.id.emailprofile);
        verified = findViewById(R.id.verify);
        clicktoverify = findViewById(R.id.emailverify);
        firebaseFirestore = FirebaseFirestore.getInstance();
        looutFrebaseUser = findViewById(R.id.logout);
        resetPassword = findViewById(R.id.reset_password);
        deleteFirebaseUser = findViewById(R.id.delete_account);


        checkVerificationOfEmail();
        retreiveFirestoreUserData();


        looutFrebaseUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutpassword();
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


    }

    private void logoutpassword() {

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
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this,"Password updated",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        }
                        else {
                            Toast.makeText(SettingsActivity.this,"Password not updated",Toast.LENGTH_LONG).show();

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

}