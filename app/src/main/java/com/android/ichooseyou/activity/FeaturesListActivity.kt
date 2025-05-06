package com.android.ichooseyou.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class FeaturesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_features_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize all buttons and set click listeners
        val coinTossButton = findViewById<MaterialButton>(R.id.coin_toss_button)
        coinTossButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    CoinTossActivity::class.java
                )
            )
        }
        val diceRollButton = findViewById<MaterialButton>(R.id.dice_roll_button)
        diceRollButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    DiceRollActivity::class.java
                )
            )
        }
        val numberGeneratorButton = findViewById<MaterialButton>(R.id.number_generator_button)
        numberGeneratorButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    NumberGeneratorActivity::class.java
                )
            )
        }
        val listPickerButton = findViewById<MaterialButton>(R.id.list_picker_button)
        listPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    ListPickerActivity::class.java
                )
            )
        }
        val teamPickerButton = findViewById<MaterialButton>(R.id.team_picker_button)
        teamPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    TeamPickerActivity::class.java
                )
            )
        }
        val colorPickerButton = findViewById<MaterialButton>(R.id.color_picker_button)
        colorPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    ColorPickerActivity::class.java
                )
            )
        }
        val decisionMakerButton = findViewById<MaterialButton>(R.id.decision_maker_button)
        decisionMakerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    DecisionMakerActivity::class.java
                )
            )
        }
        val lotteryButton = findViewById<MaterialButton>(R.id.lottery_button)
        lotteryButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    LotteryActivity::class.java
                )
            )
        }
        val wheelOfNamesButton = findViewById<MaterialButton>(R.id.wheel_of_names_button)
        wheelOfNamesButton.setOnClickListener {
            startActivity(
                Intent(
                    this@FeaturesListActivity,
                    WheelOfNamesActivity::class.java
                )
            )
        }
    }
}