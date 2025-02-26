package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toLandingPage: ImageButton = findViewById(R.id.to_landing_page)
        val logoutButton: MaterialButton = findViewById(R.id.logout_button)
        val settingsButton: MaterialButton = findViewById(R.id.settings_button)

        val profileUsername: TextView = findViewById(R.id.profile_username)
        val profileEmail: TextView = findViewById(R.id.profile_email)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        profileUsername.text = receivedName
        profileEmail.text = receivedEmail

        toLandingPage.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logging out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}