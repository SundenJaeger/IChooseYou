package com.android.ichooseyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.ichooseyou.model.User;
import com.android.ichooseyou.model.UserManager;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private User originalUser;
    private TextInputEditText editUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        originalUser = getIntent().getParcelableExtra("USER_DATA2");
        editUsername = findViewById(R.id.edit_username);

        if (originalUser != null) {
            editUsername.setText(originalUser.getName());
        }

        MaterialButton saveButton = findViewById(R.id.save_profile_button);
        MaterialButton cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> saveChanges());
        cancelButton.setOnClickListener(e -> finish());
    }

    private void saveChanges() {
        String newUsername = editUsername.getText().toString().trim();
        String originalUsername = originalUser.getName();

        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newUsername.equals(originalUsername)) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if (UserManager.INSTANCE.isUserExists(newUsername)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        originalUser.setName(newUsername);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_USER", originalUser);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}