package com.example.sociativeadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private Button BackButton;
    private ImageView TandC, Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            BackButton = findViewById(R.id.BackButton);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.this, AdminHome.class));
                    finish();
                }
            });

            TandC = findViewById(R.id.TandC);
            TandC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.this, TermsandConditions.class));
                    finish();
                }
            });

            Update = findViewById(R.id.UpdateIV);
            Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.this, UpdateProfile.class));
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "ConnectWithXML" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}