package com.android.ichooseyou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.ichooseyou.model.User;
import com.android.ichooseyou.model.UserManager;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText usernameField;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private CheckBox termsCheckbox;
    private MaterialButton createAccountButton;
    private TextView backTextButton;

    private String username, password, email;
    private boolean agreed;

    private final List<User> userList = UserManager.INSTANCE.getUserList();
    private final StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        usernameField = findViewById(R.id.username);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        termsCheckbox = findViewById(R.id.terms_checkbox);

        createAccountButton = findViewById(R.id.register_button);
        backTextButton = findViewById(R.id.back_button);

        createAccountButton.setOnClickListener(e -> checkCredentials());
        backTextButton.setOnClickListener(e -> backToLogin());
    }

    private void checkCredentials() {
        username = Objects.requireNonNull(usernameField.getText()).toString().trim();
        password = Objects.requireNonNull(passwordField.getText()).toString().trim();
        email = Objects.requireNonNull(emailField.getText()).toString().trim();

        if (username.isEmpty()) {
            sb.append("Username field is empty.\n");
        }

        if (email.isEmpty()) {
            sb.append("Email field is empty.\n");

        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            sb.append("Invalid email.\n");
        }

        if (password.isEmpty()) {
            sb.append("Password field is empty.\n");
        }

        if (!termsCheckbox.isChecked()) {
            sb.append("Check Terms & Conditions to proceed.\n");
        }

        if (sb.length() > 0) {
            showAlertDialog(sb.toString());

        } else {
            if (UserManager.INSTANCE.isUserDuplicate(username)) {
                showAlertDialog(username + " already exist.");

            } else {
                User user = new User(username, password, email);
                userList.add(user);

                Intent intent = new Intent();
                intent.putExtra("REG_USER", user);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void showAlertDialog(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information");
        builder.setMessage(errorMsg);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        sb.setLength(0);

        builder.create().show();
    }

    private void backToLogin() {
        finish();
    }
}