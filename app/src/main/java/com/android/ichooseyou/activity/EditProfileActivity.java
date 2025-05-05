package com.android.ichooseyou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou.model.User;
import com.android.ichooseyou.model.UserManager;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;


public class EditProfileActivity extends AppCompatActivity {
    private User originalUser;
    private TextInputEditText editUsername;
    private ImageView profileImageView;
    private Uri selectedImageUri = null;

    // Create activity result launcher for camera
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            profileImageView.setImageBitmap(imageBitmap);
                            // Convert bitmap to URI if needed for storage
                            selectedImageUri = getImageUri(imageBitmap);
                        }
                    }
                }
            }
    );

    // Create activity result launcher for gallery
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        originalUser = getIntent().getParcelableExtra("USER_DATA2");
        editUsername = findViewById(R.id.edit_username);
        profileImageView = findViewById(R.id.edit_profile_image);

        // Initialize profile image change button
        FloatingActionButton changeProfileImageButton = findViewById(R.id.change_profile_image);

        if (originalUser != null) {
            editUsername.setText(originalUser.getName());

            // Load profile image if available
            if (originalUser.getProfileImageUri() != null && !originalUser.getProfileImageUri().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(originalUser.getProfileImageUri());
                    selectedImageUri = imageUri; // Store the current image URI
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Could not load profile image", Toast.LENGTH_SHORT).show();
                    profileImageView.setImageResource(R.drawable.user);
                } catch (Exception e) {
                    e.printStackTrace();
                    profileImageView.setImageResource(R.drawable.user);
                }
            } else {
                profileImageView.setImageResource(R.drawable.user);
            }
        }

        // Set up click listener for profile image change
        changeProfileImageButton.setOnClickListener(v -> showImagePickerOptions());

        MaterialButton saveButton = findViewById(R.id.save_profile_button);
        MaterialButton cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> saveChanges());
        cancelButton.setOnClickListener(e -> finish());
    }

    private void showImagePickerOptions() {
        // Create a dialog to choose between camera and gallery
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose an option");

        String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Take photo with camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    cameraLauncher.launch(cameraIntent);
                } else {
                    Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
                }
            } else if (which == 1) {
                // Choose from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(galleryIntent);
            } else {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void saveChanges() {
        String newUsername = editUsername.getText().toString().trim();
        String originalUsername = originalUser.getName();

        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if anything changed
        boolean usernameChanged = !newUsername.equals(originalUsername);
        boolean imageChanged = (selectedImageUri != null &&
                (originalUser.getProfileImageUri() == null ||
                        !selectedImageUri.toString().equals(originalUser.getProfileImageUri())));

        if (!usernameChanged && !imageChanged) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        // Check if new username already exists (only if username changed)
        if (usernameChanged && UserManager.INSTANCE.isUserExists(newUsername)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create updated user with new data
        User updatedUser = new User(newUsername, originalUser.getPassword(), originalUser.getEmail());

        // Set profile image URI if we have one
        if (selectedImageUri != null) {
            updatedUser.setProfileImageUri(selectedImageUri.toString());
        } else if (originalUser.getProfileImageUri() != null) {
            updatedUser.setProfileImageUri(originalUser.getProfileImageUri());
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_USER", updatedUser);

        // Always include the profile image URI if available
        if (updatedUser.getProfileImageUri() != null) {
            resultIntent.putExtra("PROFILE_IMAGE_URI", updatedUser.getProfileImageUri());
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // Helper method to convert bitmap to URI
    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "ProfileImage", null);
        return Uri.parse(path);
    }
}