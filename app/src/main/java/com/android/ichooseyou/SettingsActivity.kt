package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val goBackButton: MaterialButton = findViewById(R.id.go_back_button)
        val aboutUsButton: MaterialButton = findViewById(R.id.about_us_button)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        goBackButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }

        aboutUsButton.setOnClickListener {
            val intent = Intent(this, DevPageActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }
    }
}