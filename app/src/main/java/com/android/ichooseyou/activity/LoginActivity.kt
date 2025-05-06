package com.android.ichooseyou.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou.model.UserManager
import com.android.ichooseyou.util.Alert
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var loginUsername: TextInputEditText
    private lateinit var loginPassword: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerTextButton: TextView
    private lateinit var username: String
    private lateinit var password: String
    private var user: User? = null
    private val errorMessages = mutableListOf<String>()

    // Activity result launcher with a more descriptive name
    private val registerActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // Use safe and version-appropriate way to get parcelable
            user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra("REG_USER", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra("REG_USER")
            }

            // Set login fields with user data
            user?.let { safeUser ->
                loginUsername.setText(safeUser.name)
                loginPassword.setText(safeUser.password)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Initialize views
        loginUsername = findViewById(R.id.login_username)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        registerTextButton = findViewById(R.id.button_register)

        // Set click listeners
        loginButton.setOnClickListener {
            if (canLogin()) {
                toLandingPage()
            } else {
                showLoginError()
            }
        }

        registerTextButton.setOnClickListener {
            toRegisterActivity()
        }
    }

    private fun toLandingPage() {
        val intent = Intent(this, LandingPageActivity::class.java).apply {
            putExtra("LOGIN_USER", user)
        }
        startActivity(intent)
    }

    private fun toRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        registerActivityLauncher.launch(intent)
    }

    private fun showLoginError() {
        val errorMessage = errorMessages.joinToString("\n")
        Alert(this, Alert.AlertType.INFORMATION)
            .setContent(errorMessage)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }

    private fun canLogin(): Boolean {
        // Clear previous error messages
        errorMessages.clear()

        // Get input values using Kotlin property accessors
        username = loginUsername.text?.toString()?.trim() ?: ""
        password = loginPassword.text?.toString()?.trim() ?: ""

        // Validate username
        if (username.isEmpty()) {
            errorMessages.add("Username field is empty.")
            return false
        } else if (!UserManager.INSTANCE.isUserExists(username)) {
            errorMessages.add("$username doesn't exist.")
            return false
        }

        // Validate password
        if (password.isEmpty()) {
            errorMessages.add("Password field is empty.")
            return false
        }

        // Find matching user with Kotlin collections API
        user = UserManager.INSTANCE.getUserList().find {
            it.name == username && it.password == password
        }

        return user != null
    }
}