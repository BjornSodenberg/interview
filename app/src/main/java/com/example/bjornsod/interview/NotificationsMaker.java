package com.example.bjornsod.interview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class NotificationsMaker {

    private FirebaseFirestore firebaseFirestore;

    private String postId,  userId,  desc, recipientId;

    public NotificationsMaker(String postId, String userId,String recipientId, String desc) {

        if(!TextUtils.isEmpty(postId) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(desc)){

            firebaseFirestore = FirebaseFirestore.getInstance();

            final Map<String,Object> notifMap = new HashMap<>();
            notifMap.put("postId", postId);
            notifMap.put("userId", userId);
            notifMap.put("recipientId",recipientId);
            notifMap.put("desc", desc);


//            firebaseFirestore.collection("Posts").document(postId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                    String user_uid = documentSnapshot.getString("user_id");
//                    firebaseFirestore.collection("Notifications").document(user_uid).set(notifMap);
//                }
//            });


            firebaseFirestore.collection("Notifications").add(notifMap);
        }
    }


}
