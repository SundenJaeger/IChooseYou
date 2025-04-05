package com.android.ichooseyou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.ichooseyou.model.User;
import com.android.ichooseyou.model.UserManager;
import com.android.ichooseyou.util.Alert;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginUsername;
    private TextInputEditText loginPassword;
    private MaterialButton loginButton;
    private TextView registerTextButton;

    private String username, password;
    private User user = null;
    private final StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);

        loginButton = findViewById(R.id.login_button);
        registerTextButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(e -> {
            if (canLogin()) {
                toLandingPage();
            } else {
                new Alert(this, Alert.AlertType.INFORMATION)
                        .setContent(sb.toString())
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        registerTextButton.setOnClickListener(e -> toRegisterActivity());
    }

    private void toLandingPage() {
        Intent intent = new Intent(this, LandingPageActivity.class);

        intent.putExtra("LOGIN_USER", user);
        startActivity(intent);
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        launcher.launch(intent);
    }

    private boolean canLogin() {
        sb.setLength(0);

        username = Objects.requireNonNull(loginUsername.getText()).toString().trim();
        password = Objects.requireNonNull(loginPassword.getText()).toString().trim();

        if (username.isEmpty()) {
            sb.append("Username field is empty.\n");
            return false;
        } else if (!UserManager.INSTANCE.isUserExists(username)) {
            sb.append(username).append(" doesn't exist.\n");
            return false;
        }

        if (password.isEmpty()) {
            sb.append("Password field is empty.\n");
            return false;
        }

        user = UserManager.INSTANCE.getUserList().stream()
                .filter(u -> u.getName().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user == null) {
            sb.append("Password does not match.\n");
            return false;
        }

        return true;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), e -> {
        if (e.getResultCode() == RESULT_OK && e.getData() != null) {

            user = e.getData().getParcelableExtra("REG_USER");

            if (user != null) {
                loginUsername.setText(user.getName());
                loginPassword.setText(user.getPassword());
            }
        }
    });
}