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

public class ViewSingleAgent extends AppCompatActivity {

    private String URL, Username, Location, WA, IA, FA;
    private ImageView Avatar;

    private TextView Name, Comments, Whatsapp, Facebook, Instagram;

    private Button Back;
    private FirebaseFirestore objectFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_agent);

        try {
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            Whatsapp = findViewById(R.id.WhatsappCount);
            Instagram = findViewById(R.id.InstagramCount);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Back = findViewById(R.id.BackButton);
            Facebook = findViewById(R.id.FacebookCount);

            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewSingleAgent.this, ViewAgents.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("URL") && getIntent().hasExtra("Username")) {
                URL = getIntent().getStringExtra("URL");
                Username = getIntent().getStringExtra("Username");

                Location = getIntent().getStringExtra("Location");
                Comments.setText(Location);

                WA = getIntent().getStringExtra("Whatsapp");
                FA = getIntent().getStringExtra("Facebook");

                Name.setText(Username);
                IA = getIntent().getStringExtra("Instagram");

                Whatsapp.setText(WA);
                Instagram.setText(IA);

                Facebook.setText(FA);
                Glide.with(this).asBitmap().load(URL).into(Avatar);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}