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

public class RecyclerViewAdapter_notiff extends RecyclerView.Adapter<RecyclerViewAdapter_notiff.MyViewHolder> {

    private Context mContext;
    private List<Notif_post> mData;

    public RecyclerViewAdapter_notiff(Context mContext, List<Notif_post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater layoutInflater =  LayoutInflater.from(mContext);

        view = layoutInflater.inflate(R.layout.cardview_item_notiff, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.profile_img.setImageResource(mData.get(i).getThumbnail_profile());
        myViewHolder.notiff.setText(mData.get(i).getUsername() + " " + mData.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_img;
        TextView notiff;


        public MyViewHolder (View itemView) {
            super(itemView);

            profile_img = (ImageView) itemView.findViewById(R.id.profile_img_id);
            notiff = (TextView) itemView.findViewById(R.id.notification_id);

        }
    }
}
