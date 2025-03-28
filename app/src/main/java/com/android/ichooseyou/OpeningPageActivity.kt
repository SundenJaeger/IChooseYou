package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import com.android.ichooseyou2.R

class OpeningPageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.openingpage)

        // Play video
        val videoView: VideoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.pokeballzrollin
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)
        videoView.start()

        // Loop the video
        videoView.setOnCompletionListener { videoView.start() }

        // Find buttons by ID
        val loginButton: Button = findViewById(R.id.button)
        val registerButton: Button = findViewById(R.id.button2)

        // Set click listeners
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
