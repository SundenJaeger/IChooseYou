package com.android.ichooseyou.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ichooseyou.activity.EditProfileActivity;
import com.android.ichooseyou.activity.LandingPageActivity;
import com.android.ichooseyou.activity.SettingsActivity;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView profileUsername;
    private TextView profileEmail;
    private MaterialButton logoutButton;
    private MaterialButton editProfileButton;
    private MaterialButton settingsButton;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("USER_DATA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileUsername = view.findViewById(R.id.profile_username);
        profileEmail = view.findViewById(R.id.profile_email);
        logoutButton = view.findViewById(R.id.logout_button);
        editProfileButton = view.findViewById(R.id.edit_profile);
        settingsButton = view.findViewById(R.id.settings_button);

        if (user != null) {
            profileUsername.setText(user.getName());
            profileEmail.setText(user.getEmail());
        }

        editProfileButton.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("USER_DATA2", user);
            launcher.launch(intent);
        });

        settingsButton.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(e -> requireActivity().finish());

        return view;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    User updatedUser = result.getData().getParcelableExtra("UPDATED_USER");
                    if (updatedUser != null) {
                        user = updatedUser;
                        profileUsername.setText(user.getName());

                        if (getActivity() instanceof LandingPageActivity) {
                            ((LandingPageActivity) getActivity()).user = updatedUser;
                        }
                    }
                }
            }
    );
}