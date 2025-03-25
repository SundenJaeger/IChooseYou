package com.android.ichooseyou

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.android.ichooseyou2.R
import kotlin.random.Random

class RandomizerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_randomizer)

        val toLandingPageBtn: ImageButton = findViewById(R.id.to_landing_page)
        val toProfileBtn: ImageButton = findViewById(R.id.to_profile)
        val toMainBtn: ImageButton = findViewById(R.id.to_main)

        val receivedName: String? = intent.getStringExtra("username")
        val receivedEmail: String? = intent.getStringExtra("email")

        val inputField: EditText = findViewById(R.id.input_field)
        val addItemBtn: Button = findViewById(R.id.add_item_btn)
        val randomizeItemBtn: Button = findViewById(R.id.randomize_item_btn)
        val randomizeNumberBtn: Button = findViewById(R.id.randomize_number_btn)
        val resultText: TextView = findViewById(R.id.result_text)
        val itemsListText: TextView = findViewById(R.id.items_list_text)
        val numberRangeMinInput: EditText = findViewById(R.id.number_range_min)
        val numberRangeMaxInput: EditText = findViewById(R.id.number_range_max)

        val items = mutableListOf<String>()
        val numbers = mutableListOf<String>()

        addItemBtn.setOnClickListener {
            val newItem = inputField.text.toString().trim()
            if (newItem.isNotEmpty()) {
                items.add(newItem)
                inputField.text.clear()
                updateItemsList(itemsListText, items, "Items")
            }
        }

        randomizeItemBtn.setOnClickListener {
            if (items.isNotEmpty()) {
                val randomIndex = Random.nextInt(items.size)
                resultText.text = "Selected Item: ${items[randomIndex]}"
            } else {
                resultText.text = "Add some items first!"
            }
        }

        randomizeNumberBtn.setOnClickListener {
            val minInput = numberRangeMinInput.text.toString()
            val maxInput = numberRangeMaxInput.text.toString()

            if (minInput.isNotEmpty() && maxInput.isNotEmpty()) {
                try {
                    val min = minInput.toInt()
                    val max = maxInput.toInt()

                    if (min < max) {
                        val randomNumber = Random.nextInt(min, max + 1)
                        val numberEntry = "Random Number ($min-$max): $randomNumber"
                        numbers.add(numberEntry)
                        resultText.text = numberEntry
                        updateItemsList(itemsListText, numbers, "Numbers")
                    } else {
                        resultText.text = "Min must be less than Max!"
                    }
                } catch (e: NumberFormatException) {
                    resultText.text = "Please enter valid numbers!"
                }
            } else {
                resultText.text = "Enter min and max range!"
            }
        }

        toMainBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", receivedName)
            intent.putExtra("email", receivedEmail)
            startActivity(intent)
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

    private fun updateItemsList(textView: TextView, items: List<String>, prefix: String) {
        textView.text = "$prefix: ${items.joinToString(" | ")}"
    }
}