package com.example.bjornsod.interview;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EntranceFragment extends Fragment {

    Button loginButton;
    TextView RegisterLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entrance, container, false);

        loginButton = (Button) view.findViewById(R.id.button_login);
        RegisterLink = (TextView) view.findViewById(R.id.register_link);

        loginButton.setOnClickListener(buttonLoginListener);
        RegisterLink.setOnClickListener(btnListener);

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

            interview interview = (interview) getActivity();
            interview.setLoginVar(false);
            interview.saveText();

            Fragment selectedFragment = new ProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        }

    };
}
