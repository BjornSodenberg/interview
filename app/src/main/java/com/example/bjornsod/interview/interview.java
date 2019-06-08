package com.example.bjornsod.interview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class interview extends AppCompatActivity {

    private TextView mTextMessage;
    private boolean nonLogin;
    SharedPreferences sPref;
    final String SAVED_VAR = "saved_log";

    private String email_reg;

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
                    if(nonLogin){
                        selectedFragment = new EntranceFragment();
                    }else {
                        selectedFragment = new ProfileFragment();
                    }


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

        BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        loadText();

    }

    protected void onDestroy() {
        super.onDestroy();
        saveText();
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

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_VAR, String.valueOf(nonLogin));
        ed.commit();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_VAR, "");
        nonLogin = Boolean.valueOf(savedText);
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }

}
