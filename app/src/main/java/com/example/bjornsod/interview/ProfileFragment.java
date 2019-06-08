package com.example.bjornsod.interview;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    private TextView ans_cnt,ask_cnt,rating_cnt,UsrName;


    public ProfileFragment() {
    }

    List<Post> lstPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        UsrName = view.findViewById(R.id.username);


        UsrName.setText("Yury");


        ans_cnt = view.findViewById(R.id.ansCount);
        ask_cnt = view.findViewById(R.id.askCount);
        rating_cnt = view.findViewById(R.id.ratingCount);


        ans_cnt.setText("5");
        ask_cnt.setText("5");
        rating_cnt.setText("5");


        Button button = (Button)view.findViewById(R.id.exit_btn);

        button.setOnClickListener(btnListener);

        lstPost = new ArrayList<>();
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.tom));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.palma));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.pammi));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.john));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter_min recyclerViewAdapter = new RecyclerViewAdapter_min(getActivity(),lstPost);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;

    }

    private View.OnClickListener btnListener = new View.OnClickListener()
    {

        public void onClick(View v)
        {
            interview interview = (interview) getActivity();
            if(!interview.getLoginVar()){
                interview.setLoginVar(true);
                interview.saveText();
            }


            if(interview.getLoginVar()) {
                Fragment selectedFragment = new EntranceFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

        }

    };
}
