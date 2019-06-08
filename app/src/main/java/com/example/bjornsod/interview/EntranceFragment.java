package com.example.bjornsod.interview;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EntranceFragment extends Fragment {

    private TextView registerlink;
    private Button buttonLogin;


    private TextView email_input,password_input;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entrance, container, false);


        email_input = (TextView)view.findViewById(R.id.email_login);
        password_input = (TextView)view.findViewById(R.id.password_login);

        registerlink = (TextView)view.findViewById(R.id.register_link);
        registerlink.setOnClickListener(btnListener);

        buttonLogin = (Button) view.findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(buttonLoginListener);


        return view;
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

            String email_input_string = email_input.getText().toString();
            String password_input_string = password_input.getText().toString();


            interview interview = (interview) getActivity();
            interview.setLoginVar(false);
            interview.saveText();

            Fragment selectedFragment = new ProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

    };
}
