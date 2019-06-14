package com.example.bjornsod.interview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotifViewHolder>{

    public Context context;
    public List<Notif_post> post_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public NotificationRecyclerViewAdapter(List<Notif_post> post_list) {
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notiffications_list_item, viewGroup, false);

        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new NotificationRecyclerViewAdapter.NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotifViewHolder notifViewHolder, int i) {

        notifViewHolder.setIsRecyclable(false);

        String desc_data = post_list.get(i).getDescription();
        notifViewHolder.setDescText(desc_data);

        final String user_id = post_list.get(i).getUser_id();

        if(user_id != null){
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String username = task.getResult().getString("name");
                        String userImage = task.getResult().getString("image");

                        notifViewHolder.setUserData(username,userImage);
                    } else {

                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class NotifViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private ImageView userImg;
        private TextView username;
        private TextView desc;


        public NotifViewHolder (View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUserData(String name, String image){
            userImg = mView.findViewById(R.id.noriff_usrimage_id);
            username = mView.findViewById(R.id.notiff_user_id);

            username.setText(name);

            RequestOptions placeHolderOptions = new RequestOptions();
            placeHolderOptions.placeholder(R.drawable.defprofileimage);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOptions).load(image).into(userImg);

        }

        public void setDescText(String descText){
            desc = mView.findViewById(R.id.notiff_desc_id);
            desc.setText(descText);
        }
    }
}
