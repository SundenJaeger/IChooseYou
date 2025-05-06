package com.android.ichooseyou.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.ichooseyou.activity.SectionDetailActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var landPageUsername: TextView
    private lateinit var welcomeUser: TextView
    private lateinit var profileImage: ImageView
    private var user: User? = null
    private lateinit var sectionsContainer: LinearLayout
    private lateinit var addSectionButton: MaterialButton
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        landPageUsername = view.findViewById(R.id.land_page_username)
        welcomeUser = view.findViewById(R.id.welcome_user)
        profileImage = view.findViewById(R.id.profile_image) // Get reference to the profile image

        // Get the container where sections will be added
        sectionsContainer = view.findViewById(R.id.sections_container)

        // Get reference to the sample section card to use as template
        val sampleSectionCard = view.findViewById<MaterialCardView>(R.id.section_card)

        // Setup the sample section card click listener
        val sampleSectionView = sampleSectionCard.getRootView()
        val sampleSectionName = sampleSectionView.findViewById<TextView>(R.id.section_name)
        sampleSectionCard.setOnClickListener {
            // Launch the SectionDetailActivity instead of showing a Toast
            navigateToSection(sampleSectionName.getText().toString())
        }

        // Set up long-press listener for the sample section
        sampleSectionCard.setOnLongClickListener {
            val sectionName = sampleSectionName.getText().toString()
            showDeleteSectionDialog(sampleSectionCard, sectionName)
            true
        }

        // Get reference to the add section button
        addSectionButton = view.findViewById(R.id.add_section_button)

        // Set click listener for the add section button
        addSectionButton.setOnClickListener { showAddSectionDialog() }

        // Get reference to the FAB and set click listener
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener { showAddSectionDialog() }

        // Update UI with user data
        updateUI()
        return view
    }

    /**
     * Update UI with current user data
     */
    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        landPageUsername.text = user?.name
        welcomeUser.text = "Welcome, " + user?.name + "!"

        // Update profile image if available
        updateProfileImage()
    }

    /**
     * Update profile image based on user data
     */
    private fun updateProfileImage() {
        if (!user?.profileImageUri.isNullOrEmpty()) {
            try {
                val imageUri = Uri.parse(user?.profileImageUri)
                val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                profileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                profileImage.setImageResource(R.drawable.user)
            } catch (e: Exception) {
                e.printStackTrace()
                profileImage.setImageResource(R.drawable.user)
            }
        } else {
            profileImage.setImageResource(R.drawable.user)
        }
    }

    /**
     * Shows a dialog for the user to enter a new section name
     */
    private fun showAddSectionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Section")

        // Set up the input field
        val input = EditText(requireContext())
        input.setHint("Enter section name")

        // Add padding to the input field
        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL
        val padding = (16 * resources.displayMetrics.density).toInt()
        container.setPadding(padding, padding, padding, padding)
        container.addView(input)
        builder.setView(container)

        // Set up the buttons
        builder.setPositiveButton("Add") { _: DialogInterface?, _: Int ->
            val sectionName = input.getText().toString().trim()
            if (!TextUtils.isEmpty(sectionName)) {
                addNewSection(sectionName)
            } else {
                Toast.makeText(requireContext(), "Section name cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    /**
     * Adds a new section with the given name
     * @param sectionName The name of the section to add
     */
    private fun addNewSection(sectionName: String) {
        // Inflate the section card layout
        val sectionView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_section, sectionsContainer, false)

        // Set the section name
        val sectionNameText = sectionView.findViewById<TextView>(R.id.section_name)
        sectionNameText.text = sectionName

        // Set click listener for the section card
        val sectionCard = sectionView.findViewById<MaterialCardView>(R.id.section_card)
        sectionCard.setOnClickListener {
            // Navigate to the section's detail activity
            navigateToSection(sectionName)
        }

        // Set long-click listener for the section card
        sectionCard.setOnLongClickListener {
            // Show delete confirmation dialog
            showDeleteSectionDialog(sectionView, sectionName)
            true // Indicate that the long click was handled
        }

        // Add the new section to the container
        sectionsContainer.addView(
            sectionView,
            sectionsContainer.childCount - 1
        ) // Add before the "Add" button
        Toast.makeText(requireContext(), "Section $sectionName added", Toast.LENGTH_SHORT).show()
    }

    /**
     * Shows a dialog to confirm section deletion
     * @param sectionView The view of the section to delete
     * @param sectionName The name of the section to delete
     */
    private fun showDeleteSectionDialog(sectionView: View, sectionName: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Section")
        builder.setMessage("Are you sure you want to delete the '$sectionName' section?")

        // Set up the buttons
        builder.setPositiveButton("Delete") { _: DialogInterface?, _: Int ->
            // Remove the section from the container
            sectionsContainer.removeView(sectionView)
            Toast.makeText(requireContext(), "Section '$sectionName' deleted", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    /**
     * Navigate to the section detail activity
     * @param sectionName The name of the section to navigate to
     */
    private fun navigateToSection(sectionName: String) {
        val intent = Intent(requireContext(), SectionDetailActivity::class.java)
        intent.putExtra("SECTION_NAME", sectionName)
        startActivity(intent)
    }

    fun updateUserData(updatedUser: User) {
        user = updatedUser
        // Only update UI if the fragment is visible/created
        if (view != null) {
            updateUI() // Call updateUI instead of setting text directly
        }
    }
}