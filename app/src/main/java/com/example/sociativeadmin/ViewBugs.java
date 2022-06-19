package com.example.sociativeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sociativeadmin.ModelClass.StatusAdapterAdminClass;
import com.example.sociativeadmin.ModelClass.StatusAdapterClassClient;
import com.example.sociativeadmin.ModelClass.StatusModelClass;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewBugs extends AppCompatActivity {

    private RecyclerView rcv;
    private Button BackButton;

    private FirebaseFirestore objectFirebaseAuth;
    private StatusAdapterAdminClass objectStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bugs);

        ConnectXML();
        objectStatusAdapter.startListening();
    }

    private void ConnectXML() {
        try {
            BackButton = findViewById(R.id.BackButton);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ViewBugs.this, AdminHome.class));
                    finish();
                }
            });

            objectFirebaseAuth = FirebaseFirestore.getInstance();
            rcv = findViewById(R.id.RCV);
            addValuestoRV();
        } catch (Exception ex) {
            Toast.makeText(this, "onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Bugs");
            FirestoreRecyclerOptions<StatusModelClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelClass>().setQuery(objectQuery, StatusModelClass.class).build();
            objectStatusAdapter = new StatusAdapterAdminClass(options, ViewBugs.this);

            rcv.setLayoutManager(new LinearLayoutManager(ViewBugs.this));
            rcv.setAdapter(objectStatusAdapter);
        } catch (Exception ex) {
            Toast.makeText(ViewBugs.this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}