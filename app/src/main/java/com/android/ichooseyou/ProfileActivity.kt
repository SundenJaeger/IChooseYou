package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text

class ProfileActivity : Activity() {
    private lateinit var profileImage: ImageView
    private lateinit var profileUsername: TextView
    private lateinit var profileEmail: TextView
    private var userImageUri: Uri? = null

    private val EDIT_PROFILE_REQUEST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toLandingPage: ImageButton = findViewById(R.id.to_landing_page)
        val logoutButton: MaterialButton = findViewById(R.id.logout_button)
        val settingsButton: MaterialButton = findViewById(R.id.settings_button)
        val editProfileButton: MaterialButton = findViewById(R.id.edit_profile)

        profileImage = findViewById(R.id.profile_image)
        profileUsername = findViewById(R.id.profile_username)
        profileEmail = findViewById(R.id.profile_email)

        val profileUsername: TextView = findViewById(R.id.profile_username)
        val profileEmail: TextView = findViewById(R.id.profile_email)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        profileUsername.text = receivedName
        profileEmail.text = receivedEmail

        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("username", profileUsername.text)
            intent.putExtra("email", profileEmail.text)

            if (userImageUri != null) {
                intent.putExtra("image_uri", userImageUri.toString())
            }
            startActivityForResult(intent, EDIT_PROFILE_REQUEST)
        }

        toLandingPage.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            intent.putExtra("username", profileUsername.text)
            intent.putExtra("email", profileEmail.text)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("username", profileUsername.text)
            intent.putExtra("email", profileEmail.text)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val updatedUsername = data.getStringExtra("updated_username")
            if (!updatedUsername.isNullOrEmpty()) {
                profileUsername.text = updatedUsername
            }

            val updatedImageUri = data.getStringExtra("updated_image_uri")
            if (!updatedImageUri.isNullOrEmpty()) {
                userImageUri = Uri.parse(updatedImageUri)
                profileImage.setImageURI(userImageUri)
            }
        }
    }
}