package com.example.sociativeadmin.ModelClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sociativeadmin.R;
import com.example.sociativeadmin.ViewSingleBug;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class StatusAdapterAdminClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterAdminClass.ViewHolder> {

    private onItemClickListner listner;
    Context ctx;

    public StatusAdapterAdminClass(@NonNull FirestoreRecyclerOptions<StatusModelClass> options, Context ctx) {
        super(options);
        this.ctx = ctx;
    }

    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final StatusModelClass statusModelClass) {
        viewHolder.UserProfileName.setText(statusModelClass.getUsername());
        Glide.with(viewHolder.URL.getContext())
                .load(statusModelClass.getURL())
                .into(viewHolder.URL);

        viewHolder.UserProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ctx, ViewSingleBug.class);
                    intent.putExtra("URL", statusModelClass.getURL());

                    intent.putExtra("Username", statusModelClass.getUsername());
                    intent.putExtra("Problem", statusModelClass.getProblem());
                    ctx.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_bugs, parent, false
        );
        return new ViewHolder(singlerow);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView UserProfileName, LocationUserName, BusinessName;

        View mView;
        RelativeLayout relativeLayoutClient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            URL = itemView.findViewById(R.id.avatar);
            UserProfileName = itemView.findViewById(R.id.UserProfile);
        }
    }

    public interface onItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(onItemClickListner listner) {
        this.listner = listner;
    }
}