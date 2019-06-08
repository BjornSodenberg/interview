package com.example.bjornsod.interview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> mData;

    public RecyclerViewAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater layoutInflater =  LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.cardview_item_post, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.img_post_thumbnail.setImageResource(mData.get(i).getThumbnail());
        myViewHolder.img_profile_thumbnail.setImageResource(mData.get(i).getThumbnail_profile());
        myViewHolder.username.setText(mData.get(i).getUsername());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_post_thumbnail;
        ImageView img_profile_thumbnail;
        TextView username;

        public MyViewHolder (View itemView) {
            super(itemView);

            img_post_thumbnail = (ImageView) itemView.findViewById(R.id.post_img_id);
            img_profile_thumbnail = (ImageView) itemView.findViewById(R.id.profile_img_id);
            username = (TextView) itemView.findViewById(R.id.username_id);

        }
    }
}
