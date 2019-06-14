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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    private List<Notif_post> lstNotif;
    private RecyclerView lstNotif_view;
    private NotificationRecyclerViewAdapter recyclerViewAdapter;
    private Boolean isFirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;
    private String recipentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notif, container, false);

        progressBar = view.findViewById(R.id.notif_progressBar_id);

        lstNotif = new ArrayList<>();

        lstNotif_view = view.findViewById(R.id.recyclerview_id);

        recyclerViewAdapter = new NotificationRecyclerViewAdapter(lstNotif);
        lstNotif_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lstNotif_view.setAdapter(recyclerViewAdapter);
        lstNotif_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();


            Query first_Query = firebaseFirestore.collection("Notifications")
                    .whereEqualTo("recipientId", firebaseAuth.getCurrentUser().getUid())
                    .orderBy("postId", Query.Direction.DESCENDING);

            first_Query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if( e == null){
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                if(doc.getType() == DocumentChange.Type.ADDED){
                                    Notif_post notif_post = doc.getDocument().toObject(Notif_post.class);
                                    lstNotif.add(notif_post);
                                    recyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                }
            });


        }
        return view;
    }

}
