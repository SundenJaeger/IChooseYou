package com.android.ichooseyou

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.android.ichooseyou2.R

class LandingPageActivity : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val toProfileBtn: ImageButton = findViewById(R.id.to_profile)
        val toRandomBtn: ImageButton = findViewById(R.id.to_random)
        val landPageName: TextView = findViewById(R.id.land_page_username)
        val welcomeUser: TextView = findViewById(R.id.welcome_user)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        landPageName.text = receivedName
        welcomeUser.text = getString(R.string.welcome_message, receivedName)

        toProfileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }

        toRandomBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }
    }
}