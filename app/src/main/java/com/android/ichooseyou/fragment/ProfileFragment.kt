package com.android.ichooseyou.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.android.ichooseyou.activity.EditProfileActivity
import com.android.ichooseyou.activity.LandingPageActivity
import com.android.ichooseyou.activity.SettingsActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import java.io.IOException

class ProfileFragment : Fragment() {
    private lateinit var profileUsername: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImage: ImageView
    private lateinit var logoutButton: MaterialButton
    private lateinit var editProfileButton: MaterialButton
    private lateinit var settingsButton: MaterialButton
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("USER_DATA", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("USER_DATA")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileUsername = view.findViewById(R.id.profile_username)
        profileEmail = view.findViewById(R.id.profile_email)
        profileImage = view.findViewById(R.id.profile_image)
        logoutButton = view.findViewById(R.id.logout_button)
        editProfileButton = view.findViewById(R.id.edit_profile)
        settingsButton = view.findViewById(R.id.settings_button)
        updateUserUI()
        editProfileButton.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra("USER_DATA2", user)
            launcher.launch(intent)
        }
        settingsButton.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }
        logoutButton.setOnClickListener { requireActivity().finish() }
        return view
    }

    // Method to update UI with current user data
    private fun updateUserUI() {
        if (user != null) {
            profileUsername.text = user!!.name
            profileEmail.text = user!!.email

            if (user?.profileImageUri != null && user!!.profileImageUri!!.isNotEmpty()) {
                try {
                    val imageUri = Uri.parse(user!!.profileImageUri)
                    val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
                    inputStream?.use { stream ->
                        val bitmap = BitmapFactory.decodeStream(stream)
                        profileImage.setImageBitmap(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Could not load profile image", Toast.LENGTH_SHORT).show()
                    profileImage.setImageResource(R.drawable.user)
                } catch (e: Exception) {
                    e.printStackTrace()
                    profileImage.setImageResource(R.drawable.user)
                }
            } else {
                profileImage.setImageResource(R.drawable.user)
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val updatedUser = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data!!.getParcelableExtra("UPDATED_USER", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data!!.getParcelableExtra("UPDATED_USER")
            }
            if (updatedUser != null) {
                val imageUriString = result.data!!.getStringExtra("PROFILE_IMAGE_URI")
                if (!imageUriString.isNullOrEmpty()) {
                    updatedUser.profileImageUri = imageUriString
                }
                user = updatedUser
                updateUserUI()
                if (activity is LandingPageActivity) {
                    (activity as LandingPageActivity).updateUser(updatedUser)
                }
            }
        }
    }

    fun updateUserData(updatedUser: User?) {
        user = updatedUser
        // Only update UI if the fragment is visible/created
        if (view != null) {
            updateUserUI()
        }
    }
}