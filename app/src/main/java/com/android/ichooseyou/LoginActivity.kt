package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val button_login = findViewById<Button>(R.id.login_button)
        val button_register: Button = findViewById(R.id.button_register)

        val userNameField: EditText = findViewById(R.id.login_username)
        val passwordField: EditText = findViewById(R.id.login_password)

        var receivedUsername: String?
        var receivedPassword: String?
        var receivedEmail: String?

        button_login.setOnClickListener {
            receivedUsername = intent.getStringExtra("username")
            receivedPassword = intent.getStringExtra("password")
            receivedEmail = intent.getStringExtra("email")

            Log.d(
                "LoginActivity",
                "Username: $receivedUsername, Password: $receivedPassword, Email: $receivedEmail"
            )

            val sb = StringBuilder()

            if (receivedUsername != null) {
                if (userNameField.text.toString().trim().isEmpty()) {
                    sb.append("Username field is empty.\n")
                } else {
                    if (userNameField.text.toString().trim() != receivedUsername) {
                        sb.append("No matching username.\n")
                    }
                }
            } else {
                if (userNameField.text.toString().trim().isEmpty()) {
                    sb.append("Username field is empty.\n")
                } else {
                    if (userNameField.text.toString().trim() != receivedUsername) {
                        sb.append("No matching username.\n")
                    }
                }
            }

            if (receivedPassword != null) {
                if (passwordField.text.toString().trim().isEmpty()) {
                    sb.append("Password field is empty.\n")
                } else {
                    if (passwordField.text.toString()
                            .trim() != receivedPassword && userNameField.text.toString()
                            .trim() == receivedUsername
                    ) {
                        sb.append("Password do not match.\n")
                    }
                }
            } else {
                if (passwordField.text.toString().trim().isEmpty()) {
                    sb.append("Password field is empty.\n")
                } else {
                    sb.append("No matching password.\n")
                }
            }

            if (sb.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(sb.toString())
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Welcome, ${userNameField.text.toString().trim()}",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, LandingPageActivity::class.java)
                intent.putExtra("username", receivedUsername)
                intent.putExtra("email", receivedEmail)
                startActivity(intent)
                finish()
            }
        }

        button_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}