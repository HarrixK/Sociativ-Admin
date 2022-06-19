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
import com.example.sociativeadmin.ViewSingleAgent;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class StatusAdapterClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterClass.ViewHolder> {

    private onItemClickListner listner;
    Context ctx;

    public StatusAdapterClass(@NonNull FirestoreRecyclerOptions<StatusModelClass> options, Context ctx) {
        super(options);
        this.ctx = ctx;
    }

    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final StatusModelClass statusModelClass) {
        viewHolder.UserProfileName.setText(statusModelClass.getUsername());
        viewHolder.LocationUserName.setText(statusModelClass.getLocation());
        viewHolder.WhatsappUser.setText(statusModelClass.getWhatsapp());
        viewHolder.InstagramUser.setText(statusModelClass.getInstagram());
        viewHolder.FacebookUser.setText(statusModelClass.getFacebook());
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
                    Intent intent = new Intent(ctx, ViewSingleAgent.class);
                    intent.putExtra("URL", statusModelClass.getURL());

                    intent.putExtra("Username", statusModelClass.getUsername());
                    intent.putExtra("Location", statusModelClass.getLocation());

                    intent.putExtra("Whatsapp", statusModelClass.getWhatsapp());
                    intent.putExtra("Facebook", statusModelClass.getFacebook());

                    intent.putExtra("Instagram", statusModelClass.getInstagram());
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
                R.layout.single_row, parent, false
        );
        return new ViewHolder(singlerow);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView UserProfileName, LocationUserName, WhatsappUser, FacebookUser, InstagramUser;

        View mView;
        RelativeLayout MessagesRL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            WhatsappUser = itemView.findViewById(R.id.WhatsappUser);
            URL = itemView.findViewById(R.id.avatar);

            UserProfileName = itemView.findViewById(R.id.UserProfile);
            LocationUserName = itemView.findViewById(R.id.LocationUser);

            FacebookUser = itemView.findViewById(R.id.FacebookUser);
            InstagramUser = itemView.findViewById(R.id.InstagramUser);

            MessagesRL = itemView.findViewById(R.id.relativeLayoutMessage);
        }
    }

    public interface onItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(onItemClickListner listner) {
        this.listner = listner;
    }
}