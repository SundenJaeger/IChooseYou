package com.android.ichooseyou.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;

public class HomeFragment extends Fragment {
    private TextView landPageUsername, welcomeUser;
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

        // Get the container where sections will be added
        sectionsContainer = view.findViewById(R.id.sections_container);

        // Get reference to the sample section card to use as template
        MaterialCardView sampleSectionCard = view.findViewById(R.id.section_card);

        // Setup the sample section card click listener
        View sampleSectionView = sampleSectionCard.getRootView();
        TextView sampleSectionName = sampleSectionView.findViewById(R.id.section_name);

        sampleSectionCard.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Sample Section clicked", Toast.LENGTH_SHORT).show();
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

        if (user != null) {
            landPageUsername.setText(user.getName());
            welcomeUser.setText("Welcome, " + user.getName() + "!");
        }

        return view;
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
            // Handle section click
            Toast.makeText(requireContext(), "Section " + sectionName + " clicked", Toast.LENGTH_SHORT).show();

            // Here you would typically navigate to the section's content
            // For example:
            // navigateToSection(sectionName);
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
     * Example method to navigate to a section's content
     * Not implemented in this example
     */
    private void navigateToSection(String sectionName) {
        // Implementation would depend on your app's navigation requirements
        // For example, you might create a new fragment and pass the section name
    }
}