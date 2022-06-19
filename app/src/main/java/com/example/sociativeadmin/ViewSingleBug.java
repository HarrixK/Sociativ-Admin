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

public class ViewSingleBug extends AppCompatActivity {

    private String URL, Username, Problem;
    private ImageView Avatar;

    private TextView Name, Comments;

    private Button Back;
    private FirebaseFirestore objectFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_bug);

        try {
            Avatar = findViewById(R.id.avatarIV);
            Name = findViewById(R.id.Name);

            Comments = findViewById(R.id.Comment);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            Back = findViewById(R.id.BackButton);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewSingleBug.this, ViewBugs.class);
                    startActivity(intent);
                }
            });

            if (getIntent().hasExtra("URL") && getIntent().hasExtra("Username")) {
                URL = getIntent().getStringExtra("URL");
                Username = getIntent().getStringExtra("Username");

                Problem = getIntent().getStringExtra("Problem");
                Comments.setText(Problem);

                Name.setText(Username);
                Glide.with(this).asBitmap().load(URL).into(Avatar);
            }
        } catch (
                Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}