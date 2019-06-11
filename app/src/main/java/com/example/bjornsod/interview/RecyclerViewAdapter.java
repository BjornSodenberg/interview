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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<Post> post_list;
    public Context context;

    public RecyclerViewAdapter(List<Post> post_list) {
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup,false);

        context = viewGroup.getContext();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String desc_data = post_list.get(i).getDesc();
        myViewHolder.setDescText(desc_data);

        String image_url = post_list.get(i).getImage_url();
        myViewHolder.setPostImage(image_url);

        String user_id = post_list.get(i).getUser_id();
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



        public MyViewHolder (View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDescText(String descText){
            descView = mView.findViewById(R.id.blogDesc_id);
            descView.setText(descText);
        }

        public void setPostImage(String downloadUri){

            postImageView = mView.findViewById(R.id.blogImage_id);
            Glide.with(context).load(downloadUri).into(postImageView);

        }

        public void setUsernameText(String usernameText){
            descView = mView.findViewById(R.id.username_id);
            descView.setText(usernameText);
        }

        public void setTime(String date){
            postDate = mView.findViewById(R.id.blogDate_id);
            postDate.setText(date);
        }
    }
}
