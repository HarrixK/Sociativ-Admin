package com.example.sociativeadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    public static int Splash = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent splashScreen = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(splashScreen);
                    finish();
                }
            }, Splash);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}