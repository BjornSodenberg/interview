package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment{

    private List<Post> lstPost;

    private RecyclerView lstPost_view;
    private RecyclerViewAdapter recyclerViewAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    private EditText searchText;
    private Button searchButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = view.findViewById(R.id.search);
        searchButton = view.findViewById(R.id.search_button);

        lstPost = new ArrayList<>();
        lstPost_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new RecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstPost_view.setAdapter(recyclerViewAdapter);
        lstPost_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() != null) {

                    String searchTextString = searchText.getText().toString();

                    firebaseFirestore = FirebaseFirestore.getInstance();

                    Query firstQuery = firebaseFirestore.collection("Posts")
                            .orderBy("title")
                            .startAt(searchTextString)
                            .endAt(searchTextString+"\uf8ff");

                    firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                            if (!queryDocumentSnapshots.isEmpty()) {
                                if (isFirstPageFirstLoad) {
//                            lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                                    lstPost.clear();
                                }


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

                    });
                }
            }
        });


        return view;

    }


}
