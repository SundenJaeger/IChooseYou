package com.android.ichooseyou.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.util.Random

class DecisionMakerActivity : AppCompatActivity() {
    private lateinit var optionsEditText: TextInputEditText
    private lateinit var resultTextView: MaterialTextView
    private lateinit var decideButton: MaterialButton
    private lateinit var clearButton: MaterialButton
    private lateinit var shuffleButton: MaterialButton
    private lateinit var resultCard: MaterialCardView
    private lateinit var optionsRecyclerView: RecyclerView
    private lateinit var optionsAdapter: OptionsAdapter
    private val optionsList: MutableList<String> = ArrayList()
    private val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decision_maker)

        // Initialize views
        optionsEditText = findViewById(R.id.options_edit_text)
        resultTextView = findViewById(R.id.result_text_view)
        decideButton = findViewById(R.id.decide_button)
        clearButton = findViewById(R.id.clear_button)
        shuffleButton = findViewById(R.id.shuffle_button)
        resultCard = findViewById(R.id.result_card)
        optionsRecyclerView = findViewById(R.id.options_recycler_view)

        // Setup RecyclerView
        optionsAdapter = OptionsAdapter(optionsList)
        optionsRecyclerView.layoutManager = LinearLayoutManager(this)
        optionsRecyclerView.adapter = optionsAdapter

        // Set click listeners
        decideButton.setOnClickListener { makeDecision() }
        clearButton.setOnClickListener { clearOptions() }
        shuffleButton.setOnClickListener { shuffleOptions() }

        // Hide result card initially
        resultCard.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun makeDecision() {
        val optionsText = optionsEditText.getText().toString().trim()
        if (optionsText.isEmpty()) {
            Toast.makeText(this, "Please enter some options", Toast.LENGTH_SHORT).show()
            return
        }

        // Split and clean options
        val optionsArray =
            optionsText.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        optionsList.clear()
        for (option in optionsArray) {
            if (option.trim().isNotEmpty()) {
                optionsList.add(option.trim())
            }
        }
        if (optionsList.size < 2) {
            Toast.makeText(this, "Please enter at least 2 options", Toast.LENGTH_SHORT).show()
            return
        }

        // Make decision
        val selectedOption = optionsList[random.nextInt(optionsList.size)]
        resultTextView.text = selectedOption
        resultCard.visibility = View.VISIBLE

        // Update RecyclerView
        optionsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearOptions() {
        optionsEditText.setText("")
        optionsList.clear()
        optionsAdapter.notifyDataSetChanged()
        resultCard.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun shuffleOptions() {
        if (optionsList.size > 1) {
            optionsList.shuffle()
            optionsAdapter.notifyDataSetChanged()

            // Update EditText with shuffled options
            val sb = StringBuilder()
            for (option in optionsList) {
                sb.append(option).append("\n")
            }
            optionsEditText.setText(sb.toString().trim())
        }
    }
}