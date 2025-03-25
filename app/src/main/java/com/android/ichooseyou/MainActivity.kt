package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.ichooseyou2.R
import kotlin.random.Random

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toLandingPageBtn: ImageButton = findViewById(R.id.to_landing_page)
        val toProfileBtn: ImageButton = findViewById(R.id.to_profile)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        val listView: ListView = findViewById(R.id.list_view)
        val items = listOf("Randomizer", "Option 2", "Option 3") // Updated first option
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    // Navigate to RandomizerActivity when "Randomizer" is clicked
                    val intent = Intent(this, RandomizerActivity::class.java)
                    intent.putExtra("username", receivedName)
                    intent.putExtra("email", receivedEmail)
                    startActivity(intent)
                }
                // You can add more options here later
                else -> {
                    // Default navigation (can be modified later)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        toProfileBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }

        toLandingPageBtn.setOnClickListener {
            val intent = Intent(this, LandingPageActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
        }
    }
}