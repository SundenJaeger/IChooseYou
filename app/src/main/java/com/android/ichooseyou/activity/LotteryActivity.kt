package com.android.ichooseyou.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class LotteryActivity : AppCompatActivity() {
    private lateinit var numbersRecyclerView: RecyclerView
    private lateinit var generateButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var clearButton: MaterialButton
    private lateinit var resultCard: MaterialCardView
    private lateinit var adapter: LotteryNumberAdapter
    private var currentNumbers: MutableList<Int> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottery)

        // Initialize views
        numbersRecyclerView = findViewById(R.id.numbers_recycler)
        generateButton = findViewById(R.id.generate_button)
        saveButton = findViewById(R.id.save_button)
        clearButton = findViewById(R.id.clear_button)
        resultCard = findViewById(R.id.result_card)

        // Setup RecyclerView
        adapter = LotteryNumberAdapter(currentNumbers)
        numbersRecyclerView.layoutManager = GridLayoutManager(this, 3)
        numbersRecyclerView.adapter = adapter

        // Set click listeners
        generateButton.setOnClickListener { generateLotteryNumbers() }
        saveButton.setOnClickListener { saveNumbers() }
        clearButton.setOnClickListener { clearNumbers() }

        // Hide result card initially
        resultCard.visibility = View.GONE
    }

    private fun generateLotteryNumbers() {
        val numbers: MutableList<Int> = ArrayList()
        for (i in 1..49) {
            numbers.add(i)
        }
        numbers.shuffle()
        currentNumbers = numbers.subList(0, 6)
        currentNumbers.sort()

        // Update UI
        adapter.updateNumbers(currentNumbers)
        resultCard.visibility = View.VISIBLE
        numbersRecyclerView.smoothScrollToPosition(0)

        // Special effect for jackpot numbers (all even or all odd)
        if (isJackpot(currentNumbers)) {
            Toast.makeText(this, "Jackpot Combination!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isJackpot(numbers: List<Int>): Boolean {
        var allEven = true
        var allOdd = true
        for (num in numbers) {
            if (num % 2 == 0) {
                allOdd = false
            } else {
                allEven = false
            }
        }
        return allEven || allOdd
    }

    private fun saveNumbers() {
        if (currentNumbers.isEmpty()) {
            Toast.makeText(this, "Generate numbers first", Toast.LENGTH_SHORT).show()
            return
        }
        // In a real app, save to database or shared preferences
        Toast.makeText(this, "Numbers saved!", Toast.LENGTH_SHORT).show()
    }

    private fun clearNumbers() {
        currentNumbers.clear()
        adapter.updateNumbers(currentNumbers)
        resultCard.visibility = View.GONE
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
    }
}