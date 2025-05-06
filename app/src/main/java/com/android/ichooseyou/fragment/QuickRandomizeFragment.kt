package com.android.ichooseyou.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.ichooseyou.activity.CoinTossActivity
import com.android.ichooseyou.activity.ColorPickerActivity
import com.android.ichooseyou.activity.DecisionMakerActivity
import com.android.ichooseyou.activity.DiceRollActivity
import com.android.ichooseyou.activity.ListPickerActivity
import com.android.ichooseyou.activity.LotteryActivity
import com.android.ichooseyou.activity.NumberGeneratorActivity
import com.android.ichooseyou.activity.TeamPickerActivity
import com.android.ichooseyou.activity.WheelOfNamesActivity
import com.android.ichooseyou.model.User
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton

class QuickRandomizeFragment : Fragment() {
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("USER_DATA", User::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("USER_DATA")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment - use the same layout as FeatureListActivity
        return inflater.inflate(R.layout.activity_features_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize all buttons and set click listeners - same as in FeatureListActivity
        val coinTossButton = view.findViewById<MaterialButton>(R.id.coin_toss_button)
        coinTossButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, CoinTossActivity::class.java
                )
            )
        }
        val diceRollButton = view.findViewById<MaterialButton>(R.id.dice_roll_button)
        diceRollButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, DiceRollActivity::class.java
                )
            )
        }
        val numberGeneratorButton = view.findViewById<MaterialButton>(R.id.number_generator_button)
        numberGeneratorButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, NumberGeneratorActivity::class.java
                )
            )
        }
        val listPickerButton = view.findViewById<MaterialButton>(R.id.list_picker_button)
        listPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, ListPickerActivity::class.java
                )
            )
        }
        val teamPickerButton = view.findViewById<MaterialButton>(R.id.team_picker_button)
        teamPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, TeamPickerActivity::class.java
                )
            )
        }
        val wheelOfNamesButton = view.findViewById<MaterialButton>(R.id.wheel_of_names_button)
        wheelOfNamesButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, WheelOfNamesActivity::class.java
                )
            )
        }
        val colorPickerButton = view.findViewById<MaterialButton>(R.id.color_picker_button)
        colorPickerButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, ColorPickerActivity::class.java
                )
            )
        }
        val decisionMakerButton = view.findViewById<MaterialButton>(R.id.decision_maker_button)
        decisionMakerButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, DecisionMakerActivity::class.java
                )
            )
        }
        val lotteryButton = view.findViewById<MaterialButton>(R.id.lottery_button)
        lotteryButton.setOnClickListener {
            startActivity(
                Intent(
                    activity, LotteryActivity::class.java
                )
            )
        }
    }
}