package com.android.ichooseyou.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.util.Random

class NumberGeneratorActivity : AppCompatActivity() {
    private lateinit var minInput: TextInputEditText
    private lateinit var maxInput: TextInputEditText
    private lateinit var resultText: MaterialTextView
    private lateinit var generateButton: MaterialButton
    private lateinit var resultCard: MaterialCardView
    private var animator: ValueAnimator? = null
    private val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_generator)

        // Initialize views
        minInput = findViewById(R.id.min_input)
        maxInput = findViewById(R.id.max_input)
        resultText = findViewById(R.id.result_text)
        resultCard = findViewById(R.id.result_card)
        generateButton = findViewById(R.id.generate_button)

        // Set click listeners
        generateButton.setOnClickListener { generateRandomNumber() }
    }

    private fun generateRandomNumber() {
        try {
            val min = minInput.getText().toString().toInt()
            val max = maxInput.getText().toString().toInt()
            if (min >= max) {
                Toast.makeText(this, "Max must be greater than min", Toast.LENGTH_SHORT).show()
                return
            }
            if (min < 0 || max < 0) {
                Toast.makeText(this, "Please enter positive numbers", Toast.LENGTH_SHORT).show()
                return
            }
            generateButton.setEnabled(false)
            resultCard.visibility = View.VISIBLE
            animateRandomNumber(min, max)
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateRandomNumber(min: Int, max: Int) {
        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }

        // Create animation that cycles through numbers
        animator = ValueAnimator.ofInt(min, max)
        animator?.setDuration(700)
        animator?.interpolator = AccelerateDecelerateInterpolator()
        animator?.repeatCount = 3
        animator?.repeatMode = ValueAnimator.REVERSE
        animator?.addUpdateListener { animation: ValueAnimator ->
            val value = animation.getAnimatedValue() as Int
            resultText.text = value.toString()
        }
        animator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val randomNumber = random.nextInt(max - min + 1) + min
                resultText.text = randomNumber.toString()
                generateButton.setEnabled(true)
                celebrateResult(randomNumber)
            }
        })
        animator?.start()
    }

    private fun celebrateResult(number: Int) {
        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        resultCard.startAnimation(pulse)
        if (isSpecialNumber(number)) {
            Toast.makeText(this, "Special Number!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isSpecialNumber(number: Int): Boolean {
        // Check for prime numbers
        if (number <= 1) return false
        if (number <= 3) return true
        if (number % 2 == 0 || number % 3 == 0) return false
        var i = 5
        while (i * i <= number) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false
            }
            i += 6
        }
        return true
    }

    override fun onDestroy() {
        if (animator != null) {
            animator?.cancel()
        }
        super.onDestroy()
    }
}