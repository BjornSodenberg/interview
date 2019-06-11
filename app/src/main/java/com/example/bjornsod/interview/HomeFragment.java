package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Post> lstPost;

    private RecyclerView lstPost_view;
    private RecyclerViewAdapter recyclerViewAdapter;

    private FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lstPost = new ArrayList<>();
        lstPost_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new RecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstPost_view.setAdapter(recyclerViewAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Post post = doc.getDocument().toObject(Post.class);
                        lstPost.add(post);

                        recyclerViewAdapter.notifyDataSetChanged();


                    }
                }
            }
        });


//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie, "Jessie", R.raw.jessie_id));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.tom, "Tom", R.raw.tom_id));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.palma, "Palma", R.raw.palma_id));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.pammi, "Pammi", R.raw.pammi_id));
//        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.john, "John", R.raw.john_id));
//
//



        return view;
    }
}
