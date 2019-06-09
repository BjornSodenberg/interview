package com.example.bjornsod.interview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView answers,questions,rating_cnt,UsrName;
    private FirebaseAuth firebaseAuth;

    private Button editButton;

    private CircleImageView selectImage;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference storageReference;
    private Uri imageUri;

    private ProgressDialog progressDialog;


    public ProfileFragment() {
    }

    List<Post> lstPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        selectImage = (CircleImageView) view.findViewById(R.id.profile_image) ;
        selectImage.setOnClickListener(this);


        storageReference = FirebaseStorage.getInstance().getReference();

        UsrName = view.findViewById(R.id.username);


        answers = view.findViewById(R.id.ansCount);
        questions = view.findViewById(R.id.askCount);
        rating_cnt = view.findViewById(R.id.ratingCount);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            Fragment selectedFragment = new EntranceFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userNameFromEmail = user.getEmail().split("@",2)[0];

        UsrName.setText(userNameFromEmail);


        editButton = (Button)view.findViewById(R.id.exit_btn);

        editButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity());

        lstPost = new ArrayList<>();
        lstPost.add(new Post("firstPost", "all", "nothing", R.raw.jessie));
        int countPosts = lstPost.size();
        questions.setText(String.valueOf(countPosts));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter_min recyclerViewAdapter = new RecyclerViewAdapter_min(getActivity(),lstPost);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(recyclerViewAdapter);


        return view;

    }

    @Override
    public void onClick(View v) {
        if(v == editButton){
            firebaseAuth.signOut();
            Fragment selectedFragment = new EntranceFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        if(v==selectImage){
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            selectImage.setImageURI(imageUri);

        }
        startPosting();
    }

    private void startPosting(){
        if(imageUri != null)
        {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
