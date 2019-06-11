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

    public List<Post> post_list;

    public RecyclerViewAdapter(List<Post> post_list) {
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String desc_data = post_list.get(i).getDesc();
        myViewHolder.setDescText(desc_data);

    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView descView;
        private View mView;

        public MyViewHolder (View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDescText(String descText){
            descView = mView.findViewById(R.id.blogDesc_id);
            descView.setText(descText);
        }
    }
}
