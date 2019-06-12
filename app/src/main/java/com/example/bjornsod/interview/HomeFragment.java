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
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Post> lstPost;

    private RecyclerView lstPost_view;
    private RecyclerViewAdapter recyclerViewAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lstPost = new ArrayList<>();
        lstPost_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new RecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstPost_view.setAdapter(recyclerViewAdapter);
        lstPost_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            lstPost_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean rechedBottom = !recyclerView.canScrollVertically(1);

                    if(rechedBottom){
                        loadMorePost();
                    }
                }
            });

            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);

            firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    if (e == null) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            if (isFirstPageFirstLoad) {
                                lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
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
                }
            });
        }

        return view;
    }

    public void loadMorePost() {

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots,FirebaseFirestoreException e) {

                    if (!queryDocumentSnapshots.isEmpty()) {


                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);

                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String postId = doc.getDocument().getId();
                                Post post = doc.getDocument().toObject(Post.class).withId(postId);
                                lstPost.add(post);

                                recyclerViewAdapter.notifyDataSetChanged();


                            }
                        }
                    }
                }
            });
        }

    }
}
