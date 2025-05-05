package com.android.ichooseyou.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.android.ichooseyou.activity.EditProfileActivity;
import com.android.ichooseyou.activity.LandingPageActivity;
import com.android.ichooseyou.activity.SettingsActivity;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private TextView profileUsername;
    private TextView profileEmail;
    private ImageView profileImage;
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
        profileImage = view.findViewById(R.id.profile_image);
        logoutButton = view.findViewById(R.id.logout_button);
        editProfileButton = view.findViewById(R.id.edit_profile);
        settingsButton = view.findViewById(R.id.settings_button);

        updateUserUI();

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

    // Method to update UI with current user data
    private void updateUserUI() {
        if (user != null) {
            profileUsername.setText(user.getName());
            profileEmail.setText(user.getEmail());

            // Set profile image if available
            if (user.getProfileImageUri() != null && !user.getProfileImageUri().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(user.getProfileImageUri());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().getContentResolver(), imageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Could not load profile image", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback to default image if there's any other error
                    profileImage.setImageResource(R.drawable.user);
                }
            } else {
                // Set default image if no profile image URI exists
                profileImage.setImageResource(R.drawable.user);
            }
        }
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    User updatedUser = result.getData().getParcelableExtra("UPDATED_USER");
                    if (updatedUser != null) {
                        // Check if there's a profile image URI returned
                        String imageUriString = result.getData().getStringExtra("PROFILE_IMAGE_URI");
                        if (imageUriString != null && !imageUriString.isEmpty()) {
                            // Update user's profile image URI
                            updatedUser.setProfileImageUri(imageUriString);
                        }

                        user = updatedUser;

                        // Update UI with the new user data
                        updateUserUI();

                        // Update user in parent activity if applicable
                        if (getActivity() instanceof LandingPageActivity) {
                            ((LandingPageActivity) getActivity()).updateUser(updatedUser);
                        }
                    }
                }
            }
    );

    public void updateUserData(User updatedUser) {
        this.user = updatedUser;
        // Only update UI if the fragment is visible/created
        if (getView() != null) {
            updateUserUI();
        }
    }
}