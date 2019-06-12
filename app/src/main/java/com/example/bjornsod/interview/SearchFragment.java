package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<Post> lstPost;
    private RecyclerView lstPost_view;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Boolean isFirstPageFirstLoad = true;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private EditText searchText;
    private Button searchBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = view.findViewById(R.id.search);
        searchBtn = view.findViewById(R.id.search_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        lstPost = new ArrayList<>();
        lstPost_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new RecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstPost_view.setAdapter(recyclerViewAdapter);
        lstPost_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchTextString = searchText.getText().toString();
                if(!TextUtils.isEmpty(searchTextString)){
                    if(firebaseAuth.getCurrentUser() != null) {

                        firebaseFirestore = FirebaseFirestore.getInstance();


                        Query firstQuery = firebaseFirestore.collection("Posts")
                                
                                .whereEqualTo("title", searchTextString);

                        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                                if (e == null) {

                                    if (!queryDocumentSnapshots.isEmpty()) {

                                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                                String postId = doc.getDocument().getId();

                                                Post post = doc.getDocument().toObject(Post.class).withId(postId);

                                                if (isFirstPageFirstLoad) {
                                                    lstPost.add(post);
                                                } else {
                                                    lstPost.add(0, post);
                                                }

                                                recyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        isFirstPageFirstLoad = false;
                                    }
                                }
                            }
                        });
                    }
                }


            }
        });





        return view;
    }
}
