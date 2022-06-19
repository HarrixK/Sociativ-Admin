package com.example.sociativeadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociativeadmin.ModelClass.StatusAdapterClass;
import com.example.sociativeadmin.ModelClass.StatusModelClass;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewAgents extends AppCompatActivity {

    private RecyclerView rcv;
    private Button BackButton;

    private FirebaseFirestore objectFirebaseAuth;
    private StatusAdapterClass objectStatusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agents);

        ConnectXML();

        objectStatusAdapter.startListening();
    }

    private void ConnectXML() {
        try {
            BackButton = findViewById(R.id.BackButton);
            BackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ViewAgents.this, AdminHome.class));
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
            Query objectQuery = objectFirebaseAuth.collection("Agents");
            FirestoreRecyclerOptions<StatusModelClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelClass>().setQuery(objectQuery, StatusModelClass.class).build();
            objectStatusAdapter = new StatusAdapterClass(options, ViewAgents.this);

            rcv.setLayoutManager(new LinearLayoutManager(ViewAgents.this));
            rcv.setAdapter(objectStatusAdapter);
        } catch (Exception ex) {
            Toast.makeText(ViewAgents.this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}