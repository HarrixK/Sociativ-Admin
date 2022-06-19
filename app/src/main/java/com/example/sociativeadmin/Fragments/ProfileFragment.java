package com.example.sociativeadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sociativeadmin.R;
import com.example.sociativeadmin.ViewAgents;
import com.example.sociativeadmin.ViewClients;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser User;

    private TextView Email, ID;
    private FirebaseFirestore objectFirebaseFirestore;

    public String id;
    private ImageView Profile, Agents, Clients, Settings;

    @Override
    public void onStart() {
        super.onStart();
        Download();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            User = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();

            Email = view.findViewById(R.id.UserMail);
            ID = view.findViewById(R.id.UserID);

            Profile = view.findViewById(R.id.profile_image);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Agents = view.findViewById(R.id.Agents);
            Clients = view.findViewById(R.id.Clients);

            User = FirebaseAuth.getInstance().getCurrentUser();
            if (User != null) {
                String name = User.getEmail().toString();
                id = User.getUid().toString();

                Email.setText(name);
                ID.setText(id);
            }

            Agents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewAgents.class);
                    startActivity(intent);
                }
            });
            Clients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewClients.class);
                    startActivity(intent);
                }
            });

            Settings = view.findViewById(R.id.SettingsButton);
            Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), com.example.sociativeadmin.Settings.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void Download() {
        try {
            objectFirebaseFirestore.collection("Admin")
                    .document(id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String url = documentSnapshot.getString("URL");
                                Glide.with(getActivity())
                                        .load(url)
                                        .into(Profile);
                            } else {
                                Toast.makeText(getActivity(), "No Such File Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed To Retrieve Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (
                Exception ex) {
            Toast.makeText(getActivity(), "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }
}