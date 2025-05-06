package com.android.ichooseyou.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.util.Random

class ColorPickerActivity : AppCompatActivity() {
    private lateinit var colorCard: MaterialCardView
    private lateinit var hexCodeText: MaterialTextView
    private lateinit var rgbText: MaterialTextView
    private lateinit var generateButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var copyButton: MaterialButton
    private var currentColor = Color.WHITE
    private val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        // Initialize views
        colorCard = findViewById(R.id.color_card)
        hexCodeText = findViewById(R.id.hex_code_text)
        rgbText = findViewById(R.id.rgb_text)
        generateButton = findViewById(R.id.generate_button)
        saveButton = findViewById(R.id.save_button)
        copyButton = findViewById(R.id.copy_button)

        // Set initial color
        updateColorDisplay(currentColor)

        // Set click listeners
        generateButton.setOnClickListener { generateRandomColor() }
        saveButton.setOnClickListener {
            // In a real app, you would save to favorites/database
            Toast.makeText(this, "Color saved to favorites", Toast.LENGTH_SHORT).show()
        }
        copyButton.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Hex Color", hexCodeText.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Color code copied", Toast.LENGTH_SHORT).show()
        }

        // Long press to manually enter color
        colorCard.setOnLongClickListener {
            // Implement color picker dialog in a real app
            Toast.makeText(this, "Long press - implement color picker", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun generateRandomColor() {
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        currentColor = Color.rgb(red, green, blue)
        updateColorDisplay(currentColor)
    }

    private fun updateColorDisplay(color: Int) {
        colorCard.setCardBackgroundColor(color)

        // Set text color based on color brightness
        val textColor = if (isColorDark(color)) Color.WHITE else Color.BLACK
        val hexCode = String.format("#%06X", 0xFFFFFF and color)
        hexCodeText.text = hexCode
        hexCodeText.setTextColor(textColor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        rgbText.text = String.format("RGB: %d, %d, %d", red, green, blue)
        rgbText.setTextColor(textColor)
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}