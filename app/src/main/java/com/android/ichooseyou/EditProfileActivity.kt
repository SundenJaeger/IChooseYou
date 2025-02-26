package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : Activity() {

    private lateinit var profileImageView: ImageView
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        profileImageView = findViewById(R.id.edit_profile_image)
        usernameEditText = findViewById(R.id.edit_username)
        emailEditText = findViewById(R.id.edit_email)
        val changeImageButton: ImageButton = findViewById(R.id.change_profile_image)
        val saveButton: Button = findViewById(R.id.save_profile_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)

        val receivedName = intent.getStringExtra("username")
        val receivedEmail = intent.getStringExtra("email")

        usernameEditText.setText(receivedName)
        emailEditText.setText(receivedEmail)

        changeImageButton.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            val newUsername = usernameEditText.text.toString()

            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()
            resultIntent.putExtra("updated_username", newUsername)

            if (selectedImageUri != null) {
                resultIntent.putExtra("updated_image_uri", selectedImageUri.toString())
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            try {
                profileImageView.setImageURI(selectedImageUri)
            } catch (e: Exception) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}