package com.example.bjornsod.interview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class interview extends AppCompatActivity {

    private TextView mTextMessage;
    private boolean nonLogin;
    SharedPreferences sPref;
    final String SAVED_VAR = "saved_log";

    private String email_reg;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private String currentUser_id;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = new HomeFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.navigation_newpost:
                    selectedFragment = new NewpostFragment();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new NotifFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new EntranceFragment();

//                    else {
//                        selectedFragment = new ProfileFragment();
//                    }


                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//        loadText();

    }

    protected void onDestroy() {
        super.onDestroy();
//        saveText();
    }

    public void setLoginVar( boolean var ) {
        nonLogin = var;
    }

    public boolean getLoginVar( ) {
        return nonLogin;
    }

    public void setEmail_reg(String email_reg) {
        this.email_reg = email_reg;
    }

    public String getEmail_reg() {
        return email_reg;
    }

//    void saveText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString(SAVED_VAR, String.valueOf(nonLogin));
//        ed.commit();
//        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
//    }
//
//    void loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        String savedText = sPref.getString(SAVED_VAR, "");
//        nonLogin = Boolean.valueOf(savedText);
//        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
//    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            Fragment selectedFragment = new EntranceFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        } else {
            currentUser_id = firebaseAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUser_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){
                            Intent setupIntent = new Intent(interview.this,AccountSetup.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(interview.this,"Error: " + errorMessage,Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
