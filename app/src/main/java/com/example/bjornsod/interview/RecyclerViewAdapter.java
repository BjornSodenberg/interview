package com.example.bjornsod.interview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<Post> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public RecyclerViewAdapter(List<Post> post_list) {
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup,false);

        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.setIsRecyclable(false);

        final String postId = post_list.get(i).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = post_list.get(i).getDesc();
        myViewHolder.setDescText(desc_data);

        String title_data = post_list.get(i).getTitle();
        myViewHolder.setTitleText(title_data);

        String image_url = post_list.get(i).getImage_url();
        myViewHolder.setPostImage(image_url);

        final String user_id = post_list.get(i).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    myViewHolder.setUserData(username,userImage,user_id);
                } else {

                }
            }
        });
//        myViewHolder.setUsernameText(user_id);

        try {
            long millisecond = post_list.get(i).getTimestamp().getTime();
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String dateString = df.format("dd/MM/yyyy", millisecond).toString();
            myViewHolder.setTime(dateString);

        } catch (Exception e){
//            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        firebaseFirestore.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if( e == null) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        myViewHolder.updateLikesCount(count);

                    } else {
                        myViewHolder.updateLikesCount(0);

                    }
                }
            }
        });

        firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null) {
                    if (documentSnapshot.exists()) {
                        myViewHolder.postLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));
                    } else {
                        myViewHolder.postLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));
                    }
                }

            }
        });

        myViewHolder.postLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firebaseAuth.getCurrentUser() != null) {

                    firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (!task.getResult().exists()) {
                                Map<String, Object> likesMap = new HashMap<>();
                                likesMap.put("timestamp", FieldValue.serverTimestamp());
                                firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).set(likesMap);

                                //            firebaseFirestore.collection("Posts").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                //                @Override
                                //                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                //
                                //                    String user_uid = documentSnapshot.getString("user_id");
                                //                    firebaseFirestore.collection("Notifications").document(user_uid).set(notifMap);
                                //                }
                                //            });

                                firebaseFirestore.collection("Posts").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        String user_uid = documentSnapshot.getString("user_id");
                                        NotificationsMaker notificationsMaker = new NotificationsMaker(postId, currentUserId, user_uid, "Like your post");
                                    }
                                });
                            } else {
                                firebaseFirestore.collection("Posts/" + postId + "/Likes").document(currentUserId).delete();
                            }

                        }
                    });
                }

            }
        });

//        comments

        myViewHolder.CommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("post_id", postId);
                context.startActivity(commentIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView descView;
        private View mView;
        private ImageView postImageView;
        private TextView username;
        private TextView postDate;
        private TextView titlePost;
        private CircleImageView user_image;

        private ImageView postLikeBtn;
        private TextView postLikeCount;

        private ImageView CommentBtn;



        public MyViewHolder (View itemView) {
            super(itemView);

            mView = itemView;
            postLikeBtn = mView.findViewById(R.id.postLike_id);
            CommentBtn = mView.findViewById(R.id.comment_icon);


        }

        public void setDescText(String descText){
            titlePost = mView.findViewById(R.id.blogDesc_id);
            titlePost.setText(descText);
        }

        public void setTitleText(String titleText){
            descView = mView.findViewById(R.id.titlePost_id);
            descView.setText(titleText);
        }

        public void setPostImage(String downloadUri){

            postImageView = mView.findViewById(R.id.blogImage_id);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.color.mlblue);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).into(postImageView);

        }

        public void setUsernameText(String usernameText){
            descView = mView.findViewById(R.id.username_id);
            descView.setText(usernameText);
        }

        public void setTime(String date){

            postDate = mView.findViewById(R.id.blogDate_id);
            postDate.setText(date);
        }

        public void setUserData(String name, String image,final String user_id){
            user_image = mView.findViewById(R.id.blogUserImage_id);
            username = mView.findViewById(R.id.blogUserName_id);

            username.setText(name);

            RequestOptions placeHolderOptions = new RequestOptions();
            placeHolderOptions.placeholder(R.drawable.defprofileimage);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOptions).load(image).into(user_image);

            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(context, ProfileActivityUser.class);
                    profileIntent.putExtra("user_id", user_id);
                    context.startActivity(profileIntent);
                }
            });

        }

        public void updateLikesCount(int count){
            postLikeCount = mView.findViewById(R.id.postLikeCount_id);
            postLikeCount.setText(count + " Likes");
        }
    }

}
