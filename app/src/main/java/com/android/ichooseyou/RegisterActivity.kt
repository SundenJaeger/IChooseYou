package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.ichooseyou2.R

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val registerButton: Button = findViewById(R.id.register_button)
        val registerBackBtn: Button = findViewById(R.id.back_button)

        val userNameField: EditText = findViewById(R.id.username)
        val passwordField: EditText = findViewById(R.id.password)
        val emailField: EditText = findViewById(R.id.email)

        var userNameText: String
        var passwordText: String
        var emailText: String

        registerButton.setOnClickListener {
            userNameText = userNameField.text.toString().trim()
            passwordText = passwordField.text.toString()
            emailText = emailField.text.toString().trim()

            val sb = StringBuilder()

            if (userNameText.isEmpty()) {
                sb.append("Username cannot be empty!\n")
            }

            if (passwordText.isEmpty()) {
                sb.append("Password cannot be empty!\n")
            }

            if (emailText.isEmpty()) {
                sb.append("Email cannot be empty!\n")
            }

            if (sb.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Registration error!")
                    .setMessage(sb.toString())
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", userNameText)
                intent.putExtra("password", passwordText)
                intent.putExtra("email", emailText)
                startActivity(intent)
                finish()
            }
        }

        registerBackBtn.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}