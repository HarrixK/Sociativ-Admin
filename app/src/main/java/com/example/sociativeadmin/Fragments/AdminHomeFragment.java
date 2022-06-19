package com.example.sociativeadmin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sociativeadmin.MainActivity;
import com.example.sociativeadmin.R;
import com.example.sociativeadmin.Settings;
import com.example.sociativeadmin.TermsandConditions;
import com.example.sociativeadmin.ViewAgents;
import com.example.sociativeadmin.ViewBugs;
import com.example.sociativeadmin.ViewClients;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminHomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser User;

    private TextView Uname, greeting;
    public ImageView Image, Settings, Agents, Bugs, Clients, LogoutBtn, TermandConditions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        try {
            User = FirebaseAuth.getInstance().getCurrentUser();
            Image = view.findViewById(R.id.ImageTime);

            Uname = view.findViewById(R.id.UserName);
            greeting = view.findViewById(R.id.Greetings);

            Settings = view.findViewById(R.id.Option1Balance);

            Agents = view.findViewById(R.id.Option2Promotion);
            Bugs = view.findViewById(R.id.Option3Location);

            Clients = view.findViewById(R.id.Option4ConnectClients);
            LogoutBtn = view.findViewById(R.id.Option5LogoutBtn);

            TermandConditions = view.findViewById(R.id.Option6TandC);
            Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Settings.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Opening Settings", Toast.LENGTH_SHORT).show();
                }
            });

            Agents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewAgents.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), "You are Viewing All Agents", Toast.LENGTH_SHORT).show();
                }
            });

            Bugs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewBugs.class);
                    startActivity(intent);
                }
            });

            Clients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ViewClients.class);
                    startActivity(intent);

                    Toast.makeText(getContext(), "You are Viewing All Clients", Toast.LENGTH_SHORT).show();
                }
            });

            LogoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                    User = FirebaseAuth.getInstance().getCurrentUser();

                    FirebaseAuth.getInstance().signOut();
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.signOut();
                    Intent intent = new Intent(getContext(), MainActivity.class);

                    startActivity(intent);
                }
            });

            TermandConditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TermsandConditions.class);
                    startActivity(intent);
                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            String s = sdf.format(new Date());

            int Hour = Integer.parseInt(s);
            if (Hour > 4 && Hour < 11) {
                greeting.setText("Good Morning");
                Image.setImageResource(R.drawable.sunvector);
                Image.isShown();
            } else if (Hour > 11 && Hour < 17) {
                greeting.setText("Good Afternoon");
                Image.setImageResource(R.drawable.sunset);
                Image.isShown();
            } else {
                greeting.setText("Good Evening");
                Image.setImageResource(R.drawable.moonvector);
                Image.isShown();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}