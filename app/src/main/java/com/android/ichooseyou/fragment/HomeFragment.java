package com.android.ichooseyou.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;

public class HomeFragment extends Fragment {
    private TextView landPageUsername, welcomeUser;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("USER_DATA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        landPageUsername = view.findViewById(R.id.land_page_username);
        welcomeUser = view.findViewById(R.id.welcome_user);

        if (user != null) {
            landPageUsername.setText(user.getName());
            welcomeUser.setText("Welcome, " + user.getName() + "!");
        }

        return view;
    }
}