package com.example.sociativeadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sociativeadmin.Fragments.AdminHomeFragment;
import com.example.sociativeadmin.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminHome extends AppCompatActivity {
    ProfileFragment objectProfileFragment;
    AdminHomeFragment objectAdminHomeFragment;

    private DrawerLayout objectDrawerLayout;
    private Toolbar objectToolbar;

    public static Context contextOfApplication;
    private NavigationView objectNavigationView;

    private ImageView headerIV;
    private FirebaseAuth mAuth;

    private FirebaseUser User;
    private TextView AgentEmailTV;

    BottomNavigationView objectBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        contextOfApplication = getApplicationContext();

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            objectAdminHomeFragment = new AdminHomeFragment();
            objectProfileFragment = new ProfileFragment();

            changeFragment(objectAdminHomeFragment);
            objectBottomNavigationView = findViewById(R.id.BNV);

            objectBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.Home:
                            changeFragment(objectAdminHomeFragment);
                            return true;

                        case R.id.Profile:
                            changeFragment(objectProfileFragment);
                            return true;

                        default:
                            return false;
                    }
                }
            });
            objectDrawerLayout = findViewById(R.id.DrawerLayout);
            objectToolbar = findViewById(R.id.OurToolBar);

            objectNavigationView = findViewById(R.id.navigationView);
            View navHeader = objectNavigationView.getHeaderView(0);

            headerIV = navHeader.findViewById(R.id.header_profileIV);
            objectNavigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            if (item.getItemId() == R.id.action_settings) {
//                                Intent intent = new Intent(AdminHome.this, Settings.class);
//                                startActivity(intent);

                                closeMyDrawer();
                                return true;
                            } else if(item.getItemId() == R.id.Logout){
                                Toast.makeText(AdminHome.this, "Loging out", Toast.LENGTH_SHORT).show();
                                User = FirebaseAuth.getInstance().getCurrentUser();

                                FirebaseAuth.getInstance().signOut();
                                mAuth = FirebaseAuth.getInstance();

                                mAuth.signOut();
                                Intent intent = new Intent(AdminHome.this, MainActivity.class);

                                startActivity(intent);
                                closeMyDrawer();

                                return true;
                            }
                            return false;
                        }
                    }
            );

            setUpHamBurgerIcon();
            AgentEmailTV=navHeader.findViewById(R.id.AgentEmail);

            User = FirebaseAuth.getInstance().getCurrentUser();
            if(User != null){
                String name = User.getEmail().toString();
                AgentEmailTV.setText(name);


            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void changeFragment(Fragment object) {
        try {
            FragmentTransaction objectFragmentTransaction = getSupportFragmentManager().beginTransaction();
            objectFragmentTransaction.replace(R.id.FrameLayout, object);

            objectFragmentTransaction.commit();
        } catch (Exception ex) {
            Toast.makeText(this, "ChangeFragment: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeMyDrawer() {
        try {
            objectDrawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "closeMyDrawer:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpHamBurgerIcon() {
        try {
            ActionBarDrawerToggle objectActionBarDrawerToggle = new ActionBarDrawerToggle(
                    this, objectDrawerLayout, objectToolbar, 0, 0
            );

            objectActionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.
                    color.colorPrimary));

            objectActionBarDrawerToggle.syncState();
        } catch (Exception e) {
            Toast.makeText(this, "setUpHamBurgerIcon:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}