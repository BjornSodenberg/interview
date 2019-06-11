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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<Post> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public RecyclerViewAdapter(List<Post> post_list) {
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup,false);

        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        String desc_data = post_list.get(i).getDesc();
        myViewHolder.setDescText(desc_data);

        String title_data = post_list.get(i).getTitle();
        myViewHolder.setTitleText(title_data);

        String image_url = post_list.get(i).getImage_url();
        myViewHolder.setPostImage(image_url);

        String user_id = post_list.get(i).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    myViewHolder.setUserData(username,userImage);
                } else {

                }
            }
        });
//        myViewHolder.setUsernameText(user_id);

        long millisecond = post_list.get(i).getTimestamp().getTime();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dateString = df.format("dd/MM/yyyy", millisecond).toString();
        myViewHolder.setTime(dateString);

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



        public MyViewHolder (View itemView) {
            super(itemView);

            mView = itemView;
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

        public void setUserData(String name, String image){
            user_image = mView.findViewById(R.id.blogUserImage_id);
            username = mView.findViewById(R.id.blogUserName_id);

            username.setText(name);

            RequestOptions placeHolderOptions = new RequestOptions();
            placeHolderOptions.placeholder(R.drawable.defprofileimage);

            Glide.with(context).applyDefaultRequestOptions(placeHolderOptions).load(image).into(user_image);

        }
    }
}
