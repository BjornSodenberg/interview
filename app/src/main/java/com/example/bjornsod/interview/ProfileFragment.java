package com.example.bjornsod.interview;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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



    public ProfileFragment() {
    }

    List<Post> lstPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        selectImage = (CircleImageView) view.findViewById(R.id.profile_image) ;
        selectImage.setOnClickListener(this);

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
    }

}

//: if request.auth == null