package com.example.bjornsod.interview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class NewpostFragment extends Fragment {

    private static final int MAX_LENGTH = 100;
    private ImageView newPostImage;
    private EditText newPostTitle;
    private EditText newPostDesc;
    private Button newPostButton;
    private ProgressBar progressBar;

    private Uri postImageUri = null;
    private  String user_id;
    private Bitmap compressedImageFile;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newpost, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        newPostImage = view.findViewById(R.id.imagePost);
        newPostTitle = view.findViewById(R.id.Title);
        newPostDesc = view.findViewById(R.id.description);
        newPostButton = view.findViewById(R.id.postButton);

        progressBar = view.findViewById(R.id.progressBar);


        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(getActivity());
            }
        });

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc= newPostDesc.getText().toString();
                final String title = newPostTitle.getText().toString();
                if (!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(title) && postImageUri != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    final String randomName = UUID.randomUUID().toString();
                    StorageReference filePath = storageReference.child("post_images").child(randomName + ".jpg");
                    filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            storageReference.child("post_images").child(randomName+".jpg").getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {

                                        String downloadUri;
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadUri = uri.toString();

                                            if(task.isSuccessful()){
                                                File newImageFile = new File(postImageUri.getPath());
                                                try {
                                                    compressedImageFile = new Compressor(getActivity())
                                                            .setMaxHeight(100)
                                                            .setMaxWidth(100)
                                                            .setQuality(2)
                                                            .compressToBitmap(newImageFile);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                                byte[] thumbData = baos.toByteArray();

                                                UploadTask uploadTask = storageReference.child("post_images/thumbs")
                                                        .child(randomName+".jpg").putBytes(thumbData);

                                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                        storageReference.child("post_images/thumbs").child(randomName+".jpg").getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                                    String downloadThumbUri;
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        downloadThumbUri = uri.toString();

                                                                        Map<String,Object> postMap = new HashMap<>();
                                                                        postMap.put("image_url",downloadUri);
                                                                        postMap.put("thumb",downloadThumbUri);
                                                                        postMap.put("title",title);
                                                                        postMap.put("desc",desc);
                                                                        postMap.put("user_id",user_id);
                                                                        postMap.put("timestamp",FieldValue.serverTimestamp());

                                                                        firebaseFirestore.collection("Posts").add(postMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                                        if(task.isSuccessful()){

                                                                                            Toast.makeText(getActivity(),"Post was added", Toast.LENGTH_SHORT).show();
                                                                                            Fragment selectedFragment = new HomeFragment();
                                                                                            getActivity().getSupportFragmentManager()
                                                                                                    .beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                                                                                        } else {


                                                                                        }

                                                                                        progressBar.setVisibility(View.INVISIBLE);

                                                                                    }
                                                                                });
                                                                    }
                                                                });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                            } else {
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        }


                                    });
                        }
                    });

                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
