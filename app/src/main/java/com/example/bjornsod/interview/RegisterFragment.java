package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    EditText usern,email,pass,cpass;
    Button register;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        usern = (EditText) view.findViewById(R.id.user_name);
        email = (EditText) view.findViewById(R.id.email_reg);
        pass = (EditText) view.findViewById(R.id.password_reg);
        cpass = (EditText) view.findViewById(R.id.password_conf);
        register = (Button) view.findViewById(R.id.button_reg);

        return view;
    }

    public void onClick(View view){

    }
}
