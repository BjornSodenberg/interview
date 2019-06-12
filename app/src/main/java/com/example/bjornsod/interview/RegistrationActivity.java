package com.example.bjornsod.interview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private TextView editTextEmail;
    private TextView editTextPassword;
    private Button registerButton;
    private TextView registerLink;
    private TextView editTextConfirmPassword;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfPassword);

        registerLink = findViewById(R.id.register_link);
        registerButton = findViewById(R.id.button_reg);

        progressBar = findViewById(R.id.regProgress);

        mAuth = FirebaseAuth.getInstance();

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confPassword = editTextConfirmPassword.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confPassword)){
                    if (password.equals(confPassword)) {
                        progressBar.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent setupIntent = new Intent(RegistrationActivity.this,AccountSetup.class);
                                    startActivity(setupIntent);
                                    finish();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegistrationActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            sendToMain();

        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegistrationActivity.this, interview.class);
        startActivity(mainIntent);
        finish();

    }
}
