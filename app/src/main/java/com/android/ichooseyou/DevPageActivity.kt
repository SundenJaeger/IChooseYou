package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class DevPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_page)

        val devGoBackButton: MaterialButton = findViewById(R.id.go_back_button_dev)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        devGoBackButton.setOnClickListener {
            val intent = Intent(this,SettingsActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }
    }
}