package com.example.bjornsod.interview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private String email_reg;
    private String current_user_id;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private String currentUser_id;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private NewpostFragment newpostFragment;
    private NotifFragment notifFragment;
    private ProfileFragment profileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            switch (item.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(homeFragment, currentFragment);
                        return true;
                    case R.id.navigation_search:
                        replaceFragment(searchFragment, currentFragment);
                        return true;
                    case R.id.navigation_newpost:
                        replaceFragment(newpostFragment, currentFragment);
                        return true;
                    case R.id.navigation_notifications:
                        replaceFragment(notifFragment, currentFragment);
                        return true;
                    case R.id.navigation_profile:
                        replaceFragment(profileFragment, currentFragment);
                        return true;

                    default:
                        return false;

            }

//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            // FRAGMENTS
            homeFragment = new HomeFragment();
            searchFragment = new SearchFragment();
            newpostFragment = new NewpostFragment();
            notifFragment = new NotifFragment();
            profileFragment = new ProfileFragment();

            initializeFragment();

            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else {
            Fragment selectedFragment = new EntranceFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

    }
//
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

            sendToLogin();
//
//            Fragment selectedFragment = new EntranceFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        } else {

            current_user_id = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(interview.this, AccountSetup.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(interview.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(interview.this,LoginActivity.class);
        startActivity(loginIntent);

//        Fragment selectedFragment = new EntranceFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        fragmentTransaction.add(R.id.fragment_container, searchFragment);
        fragmentTransaction.add(R.id.fragment_container, notifFragment);
        fragmentTransaction.add(R.id.fragment_container, newpostFragment);
        fragmentTransaction.add(R.id.fragment_container, profileFragment);

        fragmentTransaction.hide(searchFragment);
        fragmentTransaction.hide(notifFragment);
        fragmentTransaction.hide(newpostFragment);
        fragmentTransaction.hide(profileFragment);

        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == homeFragment){

            fragmentTransaction.hide(searchFragment);
            fragmentTransaction.hide(notifFragment);
            fragmentTransaction.hide(newpostFragment);
            fragmentTransaction.hide(profileFragment);

        }

        if(fragment == profileFragment){

            fragmentTransaction.hide(searchFragment);
            fragmentTransaction.hide(notifFragment);
            fragmentTransaction.hide(newpostFragment);
            fragmentTransaction.hide(homeFragment);

        }

        if(fragment == notifFragment){

            fragmentTransaction.hide(searchFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(newpostFragment);
            fragmentTransaction.hide(profileFragment);

        }

        if(fragment == newpostFragment){

            fragmentTransaction.hide(searchFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notifFragment);
            fragmentTransaction.hide(profileFragment);

        }

        if(fragment == searchFragment){

            fragmentTransaction.hide(notifFragment);
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(newpostFragment);
            fragmentTransaction.hide(profileFragment);

        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }
}