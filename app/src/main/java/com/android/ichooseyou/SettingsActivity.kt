package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val goBackButton: MaterialButton = findViewById(R.id.go_back_button)
        val aboutUsButton: MaterialButton = findViewById(R.id.about_us_button)

        goBackButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        aboutUsButton.setOnClickListener {
            val intent = Intent(this, DevPageActivity::class.java)
            startActivity(intent)
        }
    }
}