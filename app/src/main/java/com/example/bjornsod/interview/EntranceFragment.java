package com.example.bjornsod.interview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EntranceFragment extends Fragment {

    private Button loginButton;
    private TextView RegisterLink;

    private TextView editTextEmail;
    private TextView editTextPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entrance, container, false);

        loginButton = (Button) view.findViewById(R.id.button_login);
        RegisterLink = (TextView) view.findViewById(R.id.register_link);

        loginButton.setOnClickListener(buttonLoginListener);
        RegisterLink.setOnClickListener(btnListener);

        editTextEmail = (TextView) view.findViewById(R.id.editTextEmail);
        editTextPassword = (TextView) view.findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            Fragment selectedFragment = new ProfileFragment();

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

        return view;
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),getString(R.string.error_message_email),Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),getString(R.string.error_message_password),Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Entrance User");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){

                    Fragment selectedFragment = new ProfileFragment();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                } else {

                }
            }
        });
    }

    private View.OnClickListener btnListener = new View.OnClickListener()
    {

        public void onClick(View v)
        {
            Fragment selectedFragment = new RegisterFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }

    };

    private View.OnClickListener buttonLoginListener = new View.OnClickListener()
    {

        public void onClick(View v)
        {

            userLogin();

        }

    };
}
