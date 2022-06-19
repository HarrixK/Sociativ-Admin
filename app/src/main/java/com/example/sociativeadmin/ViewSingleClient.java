package com.example.sociativeadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewSingleClient extends AppCompatActivity {

    private String URL, Username, Location, Business;
    private ImageView Avatar;

    private TextView Name, Comments, BusinessName;

    private Button Back;
    private FirebaseFirestore objectFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_client);

        try {
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            BusinessName = findViewById(R.id.BName);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Back = findViewById(R.id.BackButton);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewSingleClient.this, ViewClients.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("URL") && getIntent().hasExtra("Username")) {
                URL = getIntent().getStringExtra("URL");
                Username = getIntent().getStringExtra("Username");

                Location = getIntent().getStringExtra("Location");
                Comments.setText(Location);

                Name.setText(Username);
                Business = getIntent().getStringExtra("Business");

                BusinessName.setText(Business);
                Glide.with(this).asBitmap().load(URL).into(Avatar);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}