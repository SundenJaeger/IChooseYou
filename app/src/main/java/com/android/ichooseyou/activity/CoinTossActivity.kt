package com.android.ichooseyou.activity

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import java.util.Random

class CoinTossActivity : AppCompatActivity() {
    private lateinit var coinImage: ImageView
    private lateinit var resultText: TextView
    private lateinit var backButton: Button
    private val random = Random()
    private var isTossing = false
    private var lastSide = -1 // -1 = none, 0 = heads, 1 = tails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_toss)
        coinImage = findViewById(R.id.coin_image)
        resultText = findViewById(R.id.result_text)
        backButton = findViewById(R.id.coin_toss_back_button)
        findViewById<View>(R.id.toss_button).setOnClickListener {
            if (!isTossing) {
                tossCoin()
            }
        }
        backButton.setOnClickListener { finish() }
    }

    private fun tossCoin() {
        isTossing = true
        resultText.text = "Tossing..."

        // Randomly choose heads or tails
        val side = random.nextInt(2) // 0 or 1

        // Create rotation animation
        val rotate = RotateAnimation(
            0f, (360 * 5).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.setDuration(1500)
        rotate.fillAfter = true
        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                isTossing = false
                lastSide = side
                if (side == 0) {
                    coinImage.setImageResource(R.drawable.ic_coin_heads)
                    resultText.text = "Heads!"
                } else {
                    coinImage.setImageResource(R.drawable.ic_coin_tails)
                    resultText.text = "Tails!"
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        coinImage.startAnimation(rotate)
    }
}