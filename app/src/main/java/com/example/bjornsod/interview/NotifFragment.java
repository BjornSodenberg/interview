package com.example.bjornsod.interview;

import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotifFragment extends Fragment {

    private List<Notif_post> lstPost;

    private RecyclerView lstPost_view;
    private NotificationRecyclerViewAdapter recyclerViewAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notif, container, false);

        lstPost = new ArrayList<>();
        lstPost_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new NotificationRecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstPost_view.setAdapter(recyclerViewAdapter);
        lstPost_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("Notifications").whereEqualTo("recipientId",firebaseAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        String desc = documentSnapshot.getString("desc");
                        String postId = documentSnapshot.getString("postId");
                        String recipientId = documentSnapshot.getString("recipientId");
                        String userId = documentSnapshot.getString("userId");

                        lstPost.add(new Notif_post(desc,postId,recipientId,userId));
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });


        return view;
    }

}
