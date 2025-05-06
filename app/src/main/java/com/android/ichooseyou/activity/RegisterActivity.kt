package com.android.ichooseyou.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou.model.UserManager
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Objects

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameField: TextInputEditText
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var termsCheckbox: CheckBox
    private lateinit var createAccountButton: MaterialButton
    private lateinit var backTextButton: TextView
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String
    private val userList = UserManager.INSTANCE.getUserList()
    private val sb = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        usernameField = findViewById(R.id.username)
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        termsCheckbox = findViewById(R.id.terms_checkbox)
        createAccountButton = findViewById(R.id.register_button)
        backTextButton = findViewById(R.id.back_button)
        createAccountButton.setOnClickListener { checkCredentials() }
        backTextButton.setOnClickListener { backToLogin() }
    }

    private fun checkCredentials() {
        username = Objects.requireNonNull(usernameField.getText()).toString().trim { it <= ' ' }
        password = Objects.requireNonNull(passwordField.getText()).toString().trim { it <= ' ' }
        email = Objects.requireNonNull(emailField.getText()).toString().trim { it <= ' ' }
        if (username.isEmpty()) {
            sb.append("Username field is empty.\n")
        }
        if (email.isEmpty()) {
            sb.append("Email field is empty.\n")
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())) {
            sb.append("Invalid email.\n")
        }
        if (password.isEmpty()) {
            sb.append("Password field is empty.\n")
        }
        if (!termsCheckbox.isChecked) {
            sb.append("Check Terms & Conditions to proceed.\n")
        }
        if (sb.isNotEmpty()) {
            showAlertDialog(sb.toString())
        } else {
            if (UserManager.INSTANCE.isUserDuplicate(username)) {
                showAlertDialog("$username already exist.")
            } else {
                val user = User(username, password, email)
                userList.add(user)
                val intent = Intent()
                intent.putExtra("REG_USER", user)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun showAlertDialog(errorMsg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Information")
        builder.setMessage(errorMsg)
        builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        sb.setLength(0)
        builder.create().show()
    }

    private fun backToLogin() {
        finish()
    }
}