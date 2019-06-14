package com.example.bjornsod.interview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.Manifest.*;

public class AccountSetup extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private EditText setupName;
    private Button setupSave;

    private String user_id;

    private boolean isChanged = false;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

//    private Uri downloadUrl;

    private ProgressBar setupProgress;

    private Bitmap compressedImageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();


        setupName = findViewById(R.id.notiff_user_id);
        setupSave = findViewById(R.id.saveButton);
        setupProgress= findViewById(R.id.setup_progress);

        setupProgress.setVisibility(View.VISIBLE);
        setupSave.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        setupName.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.defprofileimage);
                        Glide.with(AccountSetup.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);

                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSetup.this, "FIRESTORE retrive Error: "+error,Toast.LENGTH_SHORT).show();
                }

                setupProgress.setVisibility(View.INVISIBLE);
                setupSave.setEnabled(true);
            }
        });

        setupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = setupName.getText().toString();
                if(!TextUtils.isEmpty(user_name) && mainImageURI != null){

                    setupProgress.setVisibility(View.VISIBLE);

                    if (isChanged) {
                        user_id = firebaseAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {
                            compressedImageFile = new Compressor(AccountSetup.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (Exception e){
                            e.printStackTrace();

                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

//                        StorageReference imagePath = storageReference.child("profile_images").child(user_id+".jpg");

                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFrirestore(task, user_name);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(AccountSetup.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                    setupProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
                    } else {
                        storeFrirestore(null,user_name);
                    }
                }
            }
        });


        setupImage = findViewById(R.id.setupImage);
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(AccountSetup.this,permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(AccountSetup.this,"Permission Denied", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(AccountSetup.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    } else {
                        
                        BringImagePicker();

                    }
                } else {

                    BringImagePicker();

                }
            }
        });
    }

    private void storeFrirestore(final Task<UploadTask.TaskSnapshot> task, final String user_name) {

        storageReference.child("profile_images").child(user_id+".jpg").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Uri downloadUri;

                        if(task != null){
                            downloadUri = uri;
                        } else {
                            downloadUri = mainImageURI;
                        }

                        Map<String,String> userMap = new HashMap<>();
                        userMap.put("name",user_name);
                        userMap.put("image",downloadUri.toString());


                        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(AccountSetup.this, "The user settings are updated",Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(AccountSetup.this,interview.class);
                                    startActivity(mainIntent);
                                    finish();

                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AccountSetup.this, "FIRESTORE Error: "+error,Toast.LENGTH_SHORT).show();
                                }

                                setupProgress.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                Toast.makeText(AccountSetup.this, "download url Error: "+error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(AccountSetup.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

