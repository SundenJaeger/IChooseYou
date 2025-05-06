package com.android.ichooseyou.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Random

class DiceRollActivity : AppCompatActivity() {
    private lateinit var diceImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var historyText: TextView
    private lateinit var rollButton: MaterialButton
    private lateinit var backButton: MaterialButton
    private lateinit var resultCard: MaterialCardView
    private val random = Random()
    private var isRolling = false
    private var rollCount = 0
    private val diceFaces = intArrayOf(
        R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3,
        R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice_roll)

        // Initialize views
        diceImage = findViewById(R.id.dice_image)
        resultText = findViewById(R.id.result_text)
        historyText = findViewById(R.id.history_text)
        rollButton = findViewById(R.id.roll_button)
        backButton = findViewById(R.id.dice_roll_back_button)
        resultCard = findViewById(R.id.result_card)

        // Initialize roll count to 0
        rollCount = 0

        // Set click listeners
        rollButton.setOnClickListener {
            if (!isRolling) {
                rollDice()
            }
        }
        backButton.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun rollDice() {
        if (isRolling) return  // Prevent multiple rolls
        isRolling = true
        rollButton.setEnabled(false)
        resultText.text = "Rolling..."
        resultText.setTextColor(ContextCompat.getColor(this, R.color.rolling_text_color))
        resultCard.visibility = View.VISIBLE

        // Generate ONE random roll
        val roll = random.nextInt(6)

        // Load animations
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.setDuration(300)

        // Temporary dice face animation
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.setDuration(1000)
        animator.addUpdateListener {
            // Only show random faces during animation (not the actual result)
            var tempRoll = random.nextInt(6)
            if (tempRoll == roll) {
                tempRoll = (tempRoll + 1) % 6 // Ensure we don't show the result early
            }
            diceImage.setImageResource(diceFaces[tempRoll])
        }
        shake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                animator.start()
            }

            @SuppressLint("SetTextI18n")
            override fun onAnimationEnd(animation: Animation) {
                // Stop animations
                if (animator.isRunning) {
                    animator.cancel()
                }

                // Show final result
                diceImage.setImageResource(diceFaces[roll])
                diceImage.startAnimation(fadeIn)

                // Update UI - ONLY ONCE
                rollCount++
                val result = String.format("Roll #%d: %d", rollCount, roll + 1)
                resultText.text = result
                resultText.setTextColor(ContextCompat.getColor(this@DiceRollActivity, R.color.result_text_color))

                // Update history - ONLY ONCE
                val currentHistory = historyText.getText().toString()
                historyText.text = result + (if (currentHistory.isEmpty()) "" else "\n" + currentHistory)

                // Special effects for 6
                if (roll == 5) {
                    val celebrate =
                        AnimationUtils.loadAnimation(this@DiceRollActivity, R.anim.bounce)
                    diceImage.startAnimation(celebrate)
                }
                isRolling = false
                rollButton.setEnabled(true)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        diceImage.clearAnimation()
        diceImage.startAnimation(shake)
    }
}