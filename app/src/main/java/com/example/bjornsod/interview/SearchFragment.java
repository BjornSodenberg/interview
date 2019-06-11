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

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    List<Post> lstPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        lstPost = new ArrayList<>();
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.tom));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.palma));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.pammi));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.john));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.tom));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.palma));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.pammi));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.john));
//
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
//        RecyclerViewAdapter_min recyclerViewAdapter = new RecyclerViewAdapter_min(getActivity(),lstPost);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
//        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }
}
