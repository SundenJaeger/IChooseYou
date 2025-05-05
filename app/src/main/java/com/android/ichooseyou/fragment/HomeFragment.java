package com.android.ichooseyou.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.android.ichooseyou.activity.SectionDetailActivity;

import java.io.IOException;

public class HomeFragment extends Fragment {
    private TextView landPageUsername, welcomeUser;
    private ImageView profileImage; // Add this line to reference the profile image
    private User user;
    private LinearLayout sectionsContainer;
    private MaterialButton addSectionButton;

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
        profileImage = view.findViewById(R.id.profile_image); // Get reference to the profile image

        // Get the container where sections will be added
        sectionsContainer = view.findViewById(R.id.sections_container);

        // Get reference to the sample section card to use as template
        MaterialCardView sampleSectionCard = view.findViewById(R.id.section_card);

        // Setup the sample section card click listener
        View sampleSectionView = sampleSectionCard.getRootView();
        TextView sampleSectionName = sampleSectionView.findViewById(R.id.section_name);

        sampleSectionCard.setOnClickListener(v -> {
            // Launch the SectionDetailActivity instead of showing a Toast
            navigateToSection(sampleSectionName.getText().toString());
        });

        // Set up long-press listener for the sample section
        sampleSectionCard.setOnLongClickListener(v -> {
            String sectionName = sampleSectionName.getText().toString();
            showDeleteSectionDialog(sampleSectionCard, sectionName);
            return true;
        });

        // Get reference to the add section button
        addSectionButton = view.findViewById(R.id.add_section_button);

        // Set click listener for the add section button
        addSectionButton.setOnClickListener(v -> showAddSectionDialog());

        // Get reference to the FAB and set click listener
        com.google.android.material.floatingactionbutton.FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> showAddSectionDialog());

        // Update UI with user data
        updateUI();

        return view;
    }

    /**
     * Update UI with current user data
     */
    private void updateUI() {
        if (user != null) {
            landPageUsername.setText(user.getName());
            welcomeUser.setText("Welcome, " + user.getName() + "!");

            // Update profile image if available
            updateProfileImage();
        }
    }

    /**
     * Update profile image based on user data
     */
    private void updateProfileImage() {
        if (user != null && user.getProfileImageUri() != null && !user.getProfileImageUri().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(user.getProfileImageUri());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                // Fallback to default image if there's an error loading the image
                profileImage.setImageResource(R.drawable.user);
            } catch (Exception e) {
                e.printStackTrace();
                profileImage.setImageResource(R.drawable.user);
            }
        } else {
            // Set default image if no profile image URI exists
            profileImage.setImageResource(R.drawable.user);
        }
    }

    /**
     * Shows a dialog for the user to enter a new section name
     */
    private void showAddSectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Section");

        // Set up the input field
        final EditText input = new EditText(requireContext());
        input.setHint("Enter section name");

        // Add padding to the input field
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, padding);
        container.addView(input);

        builder.setView(container);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String sectionName = input.getText().toString().trim();
            if (!TextUtils.isEmpty(sectionName)) {
                addNewSection(sectionName);
            } else {
                Toast.makeText(requireContext(), "Section name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Adds a new section with the given name
     * @param sectionName The name of the section to add
     */
    private void addNewSection(String sectionName) {
        // Inflate the section card layout
        View sectionView = LayoutInflater.from(requireContext()).inflate(R.layout.item_section, sectionsContainer, false);

        // Set the section name
        TextView sectionNameText = sectionView.findViewById(R.id.section_name);
        sectionNameText.setText(sectionName);

        // Set click listener for the section card
        MaterialCardView sectionCard = sectionView.findViewById(R.id.section_card);
        sectionCard.setOnClickListener(v -> {
            // Navigate to the section's detail activity
            navigateToSection(sectionName);
        });

        // Set long-click listener for the section card
        sectionCard.setOnLongClickListener(v -> {
            // Show delete confirmation dialog
            showDeleteSectionDialog(sectionView, sectionName);
            return true; // Indicate that the long click was handled
        });

        // Add the new section to the container
        sectionsContainer.addView(sectionView, sectionsContainer.getChildCount() - 1);  // Add before the "Add" button

        Toast.makeText(requireContext(), "Section " + sectionName + " added", Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a dialog to confirm section deletion
     * @param sectionView The view of the section to delete
     * @param sectionName The name of the section to delete
     */
    private void showDeleteSectionDialog(View sectionView, String sectionName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Section");
        builder.setMessage("Are you sure you want to delete the '" + sectionName + "' section?");

        // Set up the buttons
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Remove the section from the container
            sectionsContainer.removeView(sectionView);
            Toast.makeText(requireContext(), "Section '" + sectionName + "' deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Navigate to the section detail activity
     * @param sectionName The name of the section to navigate to
     */
    private void navigateToSection(String sectionName) {
        Intent intent = new Intent(requireContext(), SectionDetailActivity.class);
        intent.putExtra("SECTION_NAME", sectionName);
        startActivity(intent);
    }

    public void updateUserData(User updatedUser) {
        this.user = updatedUser;
        // Only update UI if the fragment is visible/created
        if (getView() != null) {
            updateUI(); // Call updateUI instead of setting text directly
        }
    }
}