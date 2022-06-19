package com.example.sociativeadmin;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminSignup extends AppCompatActivity {

    private TextView SignInTV, Information;
    private LottieAnimationView lottie;

    private FirebaseAuth objectFirebaseAuth;
    private EditText Email, Password, Phone, Name, CPassword, Location;

    private FirebaseAuth mAuth;
    private FirebaseFirestore objectFirebaseFirestore;

    private static final int REQUEST_CODE = 124;
    private Uri imageDataInUriForm;

    private ImageView ProfileIV;
    private boolean isImageSelected = false;

    private StorageReference objectStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        ConnectWithXML();
    }

    private void openGallery() {
        try {
            Intent objectIntent = new Intent(); //Step 1:create the object of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT); //Step 2: You want to get some data

            objectIntent.setType("image/*");//Step 3: Images of all type
            startActivityForResult(objectIntent, REQUEST_CODE);

        } catch (Exception e) {
            Toast.makeText(this, "openGallery:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                imageDataInUriForm = data.getData();
                Bitmap objectBitmap;

                objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageDataInUriForm);
                ProfileIV.setImageBitmap(objectBitmap);

                isImageSelected = true;
            } else if (requestCode != REQUEST_CODE) {
                Toast.makeText(this, "Request code doesn't match", Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Fails to get image", Toast.LENGTH_SHORT).show();
            } else if (data == null) {
                Toast.makeText(this, "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(this, "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ConnectWithXML() {
        try {
            mAuth = FirebaseAuth.getInstance();
            objectFirebaseAuth = FirebaseAuth.getInstance();

            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseAuth = FirebaseAuth.getInstance();

            Button back = findViewById(R.id.BackButton);
            SignInTV = findViewById(R.id.InfoSignIn);

            lottie = findViewById(R.id.Lottie);
            Name = findViewById(R.id.editTextTextPersonName);

            Information = findViewById(R.id.InfoText);

            Email = findViewById(R.id.editTextTextEmailAddress2);
            Phone = findViewById(R.id.editTextPhone);

            Password = findViewById(R.id.editTextTextPassword2);
            ImageView Signin = findViewById(R.id.AgentSign);

            Location = findViewById(R.id.editTextLocation);
            ProfileIV = findViewById(R.id.profile_image);

            objectStorageReference = FirebaseStorage.getInstance().getReference("Users");
            CPassword = findViewById(R.id.editTextTextPassword);

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Password.getText().toString().equals(CPassword.getText().toString())) {
                        lottie.playAnimation();
                        lottie.setVisibility(View.VISIBLE);

                        Signin.setEnabled(false);
                        CheckUser();
                    } else {
                        Password.requestFocus();
                        Toast.makeText(AdminSignup.this, "Your Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ProfileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openGallery();
                    } catch (Exception e) {
                        Toast.makeText(AdminSignup.this, "Open Gallery: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AdminSignup.this, MainActivity.class));
                    finish();
                }
            });
            SignInTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AdminSignup.this, MainActivity.class));
                    finish();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "ConnectWithXML" + e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    private void CheckUser() {
        try {
            ImageView Signin = findViewById(R.id.AgentSign);
            if (!Email.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    Signin.setEnabled(false);
                    objectFirebaseAuth.fetchSignInMethodsForEmail(Email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean check = task.getResult().getSignInMethods().isEmpty();
                                    if (!check) {
                                        Signin.setEnabled(true);
                                        lottie.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AdminSignup.this, "Admin Already Exists", Toast.LENGTH_SHORT).show();

                                    } else if (check) {
//                                        lottie.setVisibility(View.INVISIBLE);

                                        Signin.setEnabled(true);
                                        Signup();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            lottie.setVisibility(View.INVISIBLE);

                            Signin.setEnabled(true);
                            Toast.makeText(AdminSignup.this, "Failed To Check If Admin Exists" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (Name.getText().toString().isEmpty()) {
                Name.requestFocus();
                lottie.setVisibility(View.INVISIBLE);

                Signin.setEnabled(true);
                Toast.makeText(this, "Name Field is empty", Toast.LENGTH_SHORT).show();
            } else {
                Email.requestFocus();
                lottie.setVisibility(View.INVISIBLE);

                Signin.setEnabled(true);
                Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ImageView Signin = findViewById(R.id.AgentSign);
            lottie.setVisibility(View.INVISIBLE);

            Signin.setEnabled(true);
            Toast.makeText(this, "Check User Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri imageDataInUriForm) {
        try {
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            String extension = objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(imageDataInUriForm));
            return extension;
        } catch (Exception e) {
            Toast.makeText(this, "getExtension:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    public void Signup() {
        try {
            ImageView Signin = findViewById(R.id.AgentSign);
            if (!Email.getText().toString().isEmpty()
                    &&
                    !Password.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    String imageName = Name.getText().toString() + "." + getExtension(imageDataInUriForm);

                    Signin.setEnabled(false);
                    objectFirebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        try {
                                            Map<String, Object> objectMap = new HashMap<>();
                                            objectMap.put("Username", Name.getText().toString());

                                            objectMap.put("Email", Email.getText().toString());
                                            objectMap.put("Phone Number", Phone.getText().toString());

                                            objectMap.put("Location", Location.getText().toString());
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            String uid = user.getUid();
                                            objectMap.put("UID", uid);

                                            objectFirebaseFirestore.collection("Admin")
                                                    .document(Name.getText().toString())
                                                    .set(objectMap)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AdminSignup.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
//                                                            lottie.setVisibility(View.INVISIBLE);
                                                        }
                                                    });

                                        } catch (Exception e) {
                                            Toast.makeText(AdminSignup.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                        uploadOurImage();
                                        Toast.makeText(AdminSignup.this, "Signed Up Successfully, Please wait", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Signin.setEnabled(true);
                                        lottie.setVisibility(View.INVISIBLE);

                                        Toast.makeText(AdminSignup.this, "Check your details again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } else if (Email.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Email.requestFocus();
                Email.setError("Email is required");
            } else if (Password.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Password.requestFocus();
                Password.setError("Password is required");
            } else if (Password.getText().toString().length() < 6) {
                Signin.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Password.requestFocus();
                Password.setError("Password must be greater than 6 characters");
            } else if (Name.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Name.requestFocus();
                Name.setError("Name is required");
            } else if (Phone.getText().toString().isEmpty()) {
                Signin.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Phone.requestFocus();
                Phone.setError("Phone Number is required");
            }

        } catch (Exception ex) {
            ImageView Signin = findViewById(R.id.AgentSign);
            lottie.setVisibility(View.INVISIBLE);

            Signin.setEnabled(true);
            Email.requestFocus();

            Toast.makeText(this, "Sign up Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadOurImage() {
        try {
            if (imageDataInUriForm != null && !Name.getText().toString().isEmpty()
                    && isImageSelected) {
                String imageName = Name.getText().toString() + "." + getExtension(imageDataInUriForm);
                final StorageReference actualImageRef = objectStorageReference.child(imageName);

                UploadTask uploadTask = actualImageRef.putFile(imageDataInUriForm);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            lottie.setVisibility(View.INVISIBLE);
                            throw task.getException();
                        }
                        return actualImageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String url = task.getResult().toString();

                            Map<String, Object> objectMap = new HashMap<>();
                            objectMap.put("Username", Name.getText().toString());

                            objectMap.put("Email", Email.getText().toString());
                            objectMap.put("Phone Number", Phone.getText().toString());

                            objectMap.put("Location", Location.getText().toString());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            String uid = user.getUid();
                            objectMap.put("UID", uid);

                            objectMap.put("URL", url);
                            objectFirebaseFirestore.collection("Admin")
                                    .document(uid)
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminSignup.this, "Failed To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            lottie.setVisibility(View.INVISIBLE);
                                            Toast.makeText(AdminSignup.this, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();

                                            startActivity(new Intent(AdminSignup.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lottie.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdminSignup.this, "Failed To Upload Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (imageDataInUriForm == null) {
                Toast.makeText(AdminSignup.this, "No Image Is Selected, kindly update your Profile", Toast.LENGTH_SHORT).show();
                lottie.setVisibility(View.INVISIBLE);

                startActivity(new Intent(AdminSignup.this, MainActivity.class));
                finish();
            } else if (Name.getText().toString().isEmpty()) {
                Toast.makeText(AdminSignup.this, "Name Field Missing", Toast.LENGTH_SHORT).show();
                Name.requestFocus();
            } else if (!isImageSelected) {
                Toast.makeText(AdminSignup.this, "Please Select An Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(AdminSignup.this, "uploadOurImage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}