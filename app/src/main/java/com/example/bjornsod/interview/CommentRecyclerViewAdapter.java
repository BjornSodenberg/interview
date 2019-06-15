package com.example.bjornsod.interview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {


    public List<Comments> commentsList;
    public Context context;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public CommentRecyclerViewAdapter(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_item, viewGroup, false);
        context = viewGroup.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new CommentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentRecyclerViewAdapter.ViewHolder viewHolder, int i) {

        viewHolder.setIsRecyclable(false);

        final String fullPath = commentsList.get(i).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        final String user_id = commentsList.get(i).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    viewHolder.setUserData(username,userImage);
                } else {

                }
            }
        });

        String commentMessage = commentsList.get(i).getMessage();
        viewHolder.setComment_message(commentMessage);

//        get Likes comment count

        firebaseFirestore.collection(fullPath + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if( e == null) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        viewHolder.updateLikesCount(count);

                    } else {
                        viewHolder.updateLikesCount(0);

                    }
                }
            }
        });

        firebaseFirestore.collection(fullPath + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null) {
                    if (documentSnapshot.exists()) {
                        viewHolder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));
                    } else {
                        viewHolder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));
                    }
                }

            }
        });

        final ArrayList<String> splitPath = new ArrayList<String> (Arrays.asList(fullPath.split("/")));


        viewHolder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firebaseAuth.getCurrentUser() != null) {

                    firebaseFirestore.collection(fullPath + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());
                                firebaseFirestore.collection(fullPath + "/Likes").document(currentUserId).set(likesMap);


                                firebaseFirestore.collection(splitPath.get(0)).document(splitPath.get(1)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        final String user_uid = documentSnapshot.getString("user_id");
                                        NotificationsMaker notificationsMaker = new NotificationsMaker(splitPath.get(1).toString(), currentUserId, user_uid, "Like your comment");
                                        firebaseFirestore.collection(fullPath + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                if( e == null) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                                            if(doc.getId().equals(user_id)){
                                                                ProfileFragment profileFragment = new ProfileFragment();
                                                                profileFragment.setRating_cnt(1);
                                                            }
                                                        }

                                                    } else {


                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                firebaseFirestore.collection(fullPath + "/Likes").document(currentUserId).delete();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private CircleImageView user_image;
        private TextView username;

        private ImageView likeBtn;
        private TextView countLikes;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            likeBtn = mView.findViewById(R.id.likeBtn_id);
        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }

        public void setUserData(String name, String image){
            user_image = mView.findViewById(R.id.comment_image);
            username = mView.findViewById(R.id.comment_username);

            username.setText(name);

            RequestOptions placeHolderOptions = new RequestOptions();
            placeHolderOptions.placeholder(R.drawable.defprofileimage);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOptions).load(image).into(user_image);

        }

        public void updateLikesCount(int count){
            countLikes = mView.findViewById(R.id.countLikes_id);
            countLikes.setText(count + " Likes");
        }

    }

}