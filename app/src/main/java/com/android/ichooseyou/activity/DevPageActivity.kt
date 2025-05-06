package com.android.ichooseyou.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class DevPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_page)
        val goBackButton = findViewById<MaterialButton>(R.id.go_back_button_dev)
        goBackButton.setOnClickListener { finish() }
    }
}