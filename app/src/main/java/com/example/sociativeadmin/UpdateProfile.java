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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    private Button BackButton, Update;
    private ImageView ProfileIV;

    private EditText Username, Location, PhoneNumberET;
    private LottieAnimationView lottie;

    private FirebaseUser User;
    private FirebaseFirestore objectFirebaseFirestore, db;

    private Uri imageDataInUriForm;
    private static final int REQUEST_CODE = 124;

    private boolean isImageSelected = false;
    private StorageReference objectStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            BackButton = findViewById(R.id.BackButton);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UpdateProfile.this, Settings.class));
                    finish();
                }
            });

            ProfileIV = findViewById(R.id.profile_image);
            Username = findViewById(R.id.Uname);

            Update = findViewById(R.id.UpdateButton);
            lottie = findViewById(R.id.Lottie);

            User = FirebaseAuth.getInstance().getCurrentUser();
            Location = findViewById(R.id.LocationEditText);
            if (User != null) {
                String name = User.getEmail().toString();
            }
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectStorageReference = FirebaseStorage.getInstance().getReference("Admin");

            FetchData();
            PhoneNumberET = findViewById(R.id.PhoneNEditText);

            ProfileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });

            Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadOurImage();
                    Update.setEnabled(false);

                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "ConnectWithXML" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    private void FetchData() {
        try {
            User = FirebaseAuth.getInstance().getCurrentUser();
            if (User != null) {
                String UID = User.getUid();
                objectFirebaseFirestore.collection("Admin")
                        .document(User.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String UsernameText = documentSnapshot.getString("Username");
                                    String LocationText = documentSnapshot.getString("Location");

                                    String URL = documentSnapshot.getString("URL");
                                    String PhoneText = documentSnapshot.getString("Phone Number");

                                    Username.setText(UsernameText);
                                    Location.setText(LocationText);

                                    PhoneNumberET.setText(PhoneText);
                                    Glide.with(UpdateProfile.this)
                                            .load(URL)
                                            .into(ProfileIV);

                                    Toast.makeText(UpdateProfile.this, "Fetching Data Successful", Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProfile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "No User Avaliable", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Fetch Data Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void uploadOurImage() {
        try {
            if (imageDataInUriForm != null && !Username.getText().toString().isEmpty()
                    && isImageSelected) {

                String imageName = Username.getText().toString() + "." + getExtension(imageDataInUriForm);
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
                            objectMap.put("Username", Username.getText().toString());
                            objectMap.put("URL", url);

                            objectMap.put("Location", Location.getText().toString());
                            objectMap.put("Phone Number", PhoneNumberET.getText().toString());

                            objectFirebaseFirestore.collection("Admin")
                                    .document(Username.getText().toString())
                                    .update(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            lottie.setVisibility(View.INVISIBLE);
                                        }
                                    });

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();

                            Map<String, Object> objectHashMap = new HashMap<>();
                            objectHashMap.put("Username", Username.getText().toString());

                            objectHashMap.put("Location", Location.getText().toString());
                            objectHashMap.put("URL", url);

                            objectHashMap.put("Phone Number", PhoneNumberET.getText().toString());
                            objectFirebaseFirestore.collection("Admin")
                                    .document(uid)
                                    .update(objectHashMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UpdateProfile.this, "Failed To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            lottie.setVisibility(View.INVISIBLE);
                                            Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                            startActivity(new Intent(UpdateProfile.this, Settings.class));
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lottie.setVisibility(View.INVISIBLE);
                        Toast.makeText(UpdateProfile.this, "Failed To Upload Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (imageDataInUriForm == null) {
                Update.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Toast.makeText(UpdateProfile.this, "No Image Is Selected", Toast.LENGTH_SHORT).show();
            } else if (Username.getText().toString().isEmpty()) {
                Update.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Toast.makeText(UpdateProfile.this, "Name Field Missing", Toast.LENGTH_SHORT).show();
                Username.requestFocus();
            } else if (!isImageSelected) {
                Update.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                Toast.makeText(UpdateProfile.this, "Please Select An Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Update.setEnabled(true);
            lottie.setVisibility(View.INVISIBLE);

            Toast.makeText(UpdateProfile.this, "uploadOurImage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}