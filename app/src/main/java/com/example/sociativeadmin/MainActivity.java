package com.example.sociativeadmin;

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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView SignUp, ResetPW;
    private Button Signin;

    private EditText Email, Password;
    private FirebaseAuth mAuth;

    private FirebaseAuth objectFirebaseAuth;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectWithXML();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Successfully Signed in", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this, AdminHome.class));
            finish();
        }
    }

    private void ConnectWithXML() {
        try {
            mAuth = FirebaseAuth.getInstance();
            objectFirebaseAuth = FirebaseAuth.getInstance();

            lottie = findViewById(R.id.Lottie);
            Email = findViewById(R.id.editTextTextEmailAddress);

            Password = findViewById(R.id.editTextTextPassword);
            Signin = findViewById(R.id.SigninButton);

            SignUp = findViewById(R.id.SignUpTV);
            SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AdminSignup.class));
                    finish();
                }
            });

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);

                    signinuser();
                }
            });

            ResetPW = findViewById(R.id.ResetPassword);
            ResetPW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText resetMail = new EditText(v.getContext());
                    AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

                    passwordResetDialog.setTitle("Reset Password?");
                    passwordResetDialog.setMessage("Enter Your Mail to Recieve Reset Link");

                    passwordResetDialog.setView(resetMail);
                    passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Mail = resetMail.getText().toString();
                            mAuth.sendPasswordResetEmail(Mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this, "Reset Link Sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Unable to Send Reset Link", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Password Reset Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    passwordResetDialog.create().show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "ConnectWithXML" + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public void signinuser() {
        try {
            if (!Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()) {
                if (objectFirebaseAuth.getCurrentUser() != null) {
                    objectFirebaseAuth.signOut();
                    Signin.setEnabled(true);

                    lottie.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Admin Logged Out Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    objectFirebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),
                            Password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Signin.setEnabled(true);

                                    Toast.makeText(MainActivity.this, "Admin Logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, AdminHome.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Signin.setEnabled(true);
                            Email.requestFocus();

                            lottie.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Failed To Sign-in Admin:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (Email.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                Email.requestFocus();

                lottie.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_SHORT).show();
            } else if (Password.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                Password.requestFocus();

                lottie.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

            Signin.setEnabled(true);
            Email.requestFocus();

            lottie.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Logging In Error" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}