package com.example.bjornsod.interview;

import android.net.Uri;
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
import android.content.Intent;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView answers,questions,rating_cnt,UsrName;
    private FirebaseAuth firebaseAuth;

    private Button editButton;

    private CircleImageView selectImage;

    private static final int GALLERY_REQUEST = 1;
    private Uri imageUri;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private List<Post> lst_Post;
    private RecyclerView lst_Post_view;
    private RecyclerViewAdapter recycler_ViewAdapter;
    private Boolean is_FirstPageFirstLoad = true;
    private DocumentSnapshot lastVisible;


    public ProfileFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        инициализация

        selectImage = (CircleImageView) view.findViewById(R.id.profile_image) ;
        selectImage.setOnClickListener(this);

        UsrName = view.findViewById(R.id.notiff_user_id);


        answers = view.findViewById(R.id.ansCount);
        questions = view.findViewById(R.id.askCount);
        rating_cnt = view.findViewById(R.id.ratingCount);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        editButton = view.findViewById(R.id.exit_btn);
        editButton.setOnClickListener(this);

//      информация о пользователе
        if(firebaseAuth.getCurrentUser() != null){
            String user_id = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String username = task.getResult().getString("name");
                        String userImage = task.getResult().getString("image");

                        selectImage = view.findViewById(R.id.profile_image);
                        UsrName = view.findViewById(R.id.notiff_user_id);

                        UsrName.setText(username);

                        RequestOptions placeHolderOptions = new RequestOptions();
                        placeHolderOptions.placeholder(R.drawable.defprofileimage);

                        Glide.with(getActivity()).applyDefaultRequestOptions(placeHolderOptions).load(userImage).into(selectImage);
                    } else {
                        Toast.makeText(getActivity(), "Wrong load", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }





//        recycler view
        lst_Post = new ArrayList<>();
        lst_Post_view = view.findViewById(R.id.recyclerview_id);

        recycler_ViewAdapter = new RecyclerViewAdapter(lst_Post);
        lst_Post_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        lst_Post_view.setAdapter(recycler_ViewAdapter);
        lst_Post_view.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();


            Query first_Query = firebaseFirestore.collection("Posts")
                    .whereEqualTo("user_id", firebaseAuth.getCurrentUser().getUid())
                    .orderBy("timestamp", Query.Direction.DESCENDING);

            first_Query.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                    if (e == null) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            if (is_FirstPageFirstLoad) {
                                lst_Post.clear();
                            }

                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String postId = doc.getDocument().getId();

                                    Post post = doc.getDocument().toObject(Post.class).withId(postId);

                                    if (is_FirstPageFirstLoad) {
                                        lst_Post.add(post);
                                    } else {
                                        lst_Post.add(0, post);
                                    }

                                    recycler_ViewAdapter.notifyDataSetChanged();
                                }
                            }

                            int countPosts = lst_Post.size();
                            questions.setText(String.valueOf(countPosts));

                            is_FirstPageFirstLoad = false;
                        }
                    }
                }
            });
        }

        return view;

    }

    @Override
    public void onClick(View v) {
        if(v == editButton){
            firebaseAuth.signOut();
            Intent mainIntent = new Intent(getActivity(), interview.class);
            startActivity(mainIntent);

        }
        if(v==selectImage){
            Intent setupAccount = new Intent(getActivity(),AccountSetup.class);
            startActivity(setupAccount);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            selectImage.setImageURI(imageUri);

        }
    }


}

//: if request.auth == null