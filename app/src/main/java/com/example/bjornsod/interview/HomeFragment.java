package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    List<Post> lstPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lstPost = new ArrayList<>();
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie, "Jessie", R.raw.jessie_id));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.tom, "Tom", R.raw.tom_id));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.palma, "Palma", R.raw.palma_id));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.pammi, "Pammi", R.raw.pammi_id));
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.john, "John", R.raw.john_id));


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),lstPost);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }
}
