package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivityUser extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView profileName;

    private TextView quesCount;
    private TextView ansCount;
    private TextView ratingCount;

    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private List<Post> lstPost;
    private RecyclerView lstPost_view;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Boolean isFirstPageFirstLoad = true;

    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity_user);

        userId = getIntent().getStringExtra("user_id");

        profileImage = findViewById(R.id.profileImage_id);
        profileName = findViewById(R.id.notiff_user_id);

        quesCount = findViewById(R.id.questCount_id);
//        ansCount = findViewById(R.id.ansCount_id);
        ratingCount = findViewById(R.id.ratingCount_id);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
//            String user_id = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String username = task.getResult().getString("name");
                        String userImage = task.getResult().getString("image");

                        profileName.setText(username);

                        RequestOptions placeHolderOptions = new RequestOptions();
                        placeHolderOptions.placeholder(R.drawable.defprofileimage);

                        Glide.with(ProfileActivityUser.this).applyDefaultRequestOptions(placeHolderOptions).load(userImage).into(profileImage);
                    } else {
                        Toast.makeText(ProfileActivityUser.this, "Wrong load", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

        //        recycler view
        lstPost = new ArrayList<>();
        lstPost_view = findViewById(R.id.recyclerView_id);

        recyclerViewAdapter = new RecyclerViewAdapter(lstPost);
        lstPost_view.setLayoutManager(new LinearLayoutManager(ProfileActivityUser.this));
        lstPost_view.setAdapter(recyclerViewAdapter);
        lstPost_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();


            Query firstQuery = firebaseFirestore.collection("Posts")
                    .whereEqualTo("user_id", userId)
                    .orderBy("timestamp", Query.Direction.DESCENDING);

            firstQuery.addSnapshotListener(ProfileActivityUser.this,new EventListener<QuerySnapshot>() {
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

                            int countPosts = lstPost.size();
                            quesCount.setText(String.valueOf(countPosts));

                            isFirstPageFirstLoad = false;
                        }
                    }
                }
            });
        }



    }
}
