package com.android.ichooseyou.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val goBackButton = findViewById<MaterialButton>(R.id.go_back_button)
        val aboutUsButton = findViewById<MaterialButton>(R.id.about_us_button)
        aboutUsButton.setOnClickListener {
            val intent = Intent(this, DevPageActivity::class.java)
            startActivity(intent)
        }
        goBackButton.setOnClickListener { finish() }
    }
}