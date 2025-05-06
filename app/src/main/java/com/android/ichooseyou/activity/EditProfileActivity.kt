package com.android.ichooseyou.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou.model.UserManager
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    private var originalUser: User? = null
    private lateinit var editUsername: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var profileImageView: ImageView
    private var selectedImageUri: Uri? = null
    private var tempCameraImageUri: Uri? = null

    // Request permissions launcher
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera launcher using the modern Activity Result API
    private val takePictureLauncher: ActivityResultLauncher<Uri> = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraImageUri?.let { uri ->
                selectedImageUri = uri
                loadImageFromUri(uri)
            }
        }
    }

    // Gallery launcher using the modern photo picker API
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            loadImageFromUri(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        editUsername = findViewById(R.id.edit_username)
        editEmail = findViewById(R.id.edit_email)
        profileImageView = findViewById(R.id.edit_profile_image)

        // Get user data from intent
        originalUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("USER_DATA2", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("USER_DATA2")
        }

        // Set up user data in views
        originalUser?.let { user ->
            editUsername.setText(user.name)
            editEmail.setText(user.email)

            // Load profile image if available
            if (!user.profileImageUri.isNullOrEmpty()) {
                try {
                    val imageUri = Uri.parse(user.profileImageUri)
                    selectedImageUri = imageUri
                    loadImageFromUri(imageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    profileImageView.setImageResource(R.drawable.user)
                }
            } else {
                profileImageView.setImageResource(R.drawable.user)
            }
        } ?: run {
            // If no user data, set default image
            profileImageView.setImageResource(R.drawable.user)
        }

        // Set up click listeners
        findViewById<FloatingActionButton>(R.id.change_profile_image).setOnClickListener {
            showImagePickerOptions()
        }

        findViewById<MaterialButton>(R.id.save_profile_button).setOnClickListener {
            saveChanges()
        }

        findViewById<MaterialButton>(R.id.cancel_button).setOnClickListener {
            finish()
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")

        AlertDialog.Builder(this)
            .setTitle("Choose an option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> pickImageFromGallery()
                    else -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        try {
            tempCameraImageUri = createImageUri()
            tempCameraImageUri?.let { uri ->
                takePictureLauncher.launch(uri)
            } ?: run {
                Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Camera error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageUri(): Uri? {
        // Create a unique file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"

        // Create the content values for the image
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        // Insert the image into MediaStore and return the URI
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private fun loadImageFromUri(uri: Uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Use ImageDecoder for API 28+
                val source = ImageDecoder.createSource(contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }
                profileImageView.setImageBitmap(bitmap)
            } else {
                // Fallback for older versions
                @Suppress("DEPRECATION")
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                profileImageView.setImageBitmap(bitmap)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveChanges() {
        val newUsername = editUsername.text.toString().trim()
        val newEmail = editEmail.text.toString().trim()
        val originalUsername = originalUser?.name

        // Validate input
        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (newEmail.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if anything changed
        val usernameChanged = newUsername != originalUsername
        val emailChanged = newEmail != originalUser?.email
        val imageChanged = selectedImageUri != null &&
                (originalUser?.profileImageUri == null ||
                        selectedImageUri.toString() != originalUser?.profileImageUri)

        if (!usernameChanged && !emailChanged && !imageChanged) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        // Check if new username already exists (only if username changed)
        if (usernameChanged && UserManager.INSTANCE.isUserExists(newUsername)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            return
        }

        // Create updated user with new data
        val updatedUser = User(newUsername, originalUser?.password, newEmail)

        // Set profile image URI if we have one
        if (selectedImageUri != null) {
            updatedUser.profileImageUri = selectedImageUri.toString()
        } else if (!originalUser?.profileImageUri.isNullOrEmpty()) {
            updatedUser.profileImageUri = originalUser?.profileImageUri
        }

        // Return the updated user data
        val resultIntent = Intent().apply {
            putExtra("UPDATED_USER", updatedUser)
            if (!updatedUser.profileImageUri.isNullOrEmpty()) {
                putExtra("PROFILE_IMAGE_URI", updatedUser.profileImageUri)
            }
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}