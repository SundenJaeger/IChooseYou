package com.android.ichooseyou.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

public class FeaturesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_features_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize all buttons and set click listeners
        MaterialButton coinTossButton = findViewById(R.id.coin_toss_button);
        coinTossButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, CoinTossActivity.class));
        });

        MaterialButton diceRollButton = findViewById(R.id.dice_roll_button);
        diceRollButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, DiceRollActivity.class));
        });

        MaterialButton numberGeneratorButton = findViewById(R.id.number_generator_button);
        numberGeneratorButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, NumberGeneratorActivity.class));
        });

        MaterialButton listPickerButton = findViewById(R.id.list_picker_button);
        listPickerButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, ListPickerActivity.class));
        });


        MaterialButton teamPickerButton = findViewById(R.id.team_picker_button);
        teamPickerButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, TeamPickerActivity.class));
        });

        MaterialButton colorPickerButton = findViewById(R.id.color_picker_button);
        colorPickerButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, ColorPickerActivity.class));
        });

        MaterialButton decisionMakerButton = findViewById(R.id.decision_maker_button);
        decisionMakerButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, DecisionMakerActivity.class));
        });

        MaterialButton lotteryButton = findViewById(R.id.lottery_button);
        lotteryButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, LotteryActivity.class));
        });

        MaterialButton wheelOfNamesButton = findViewById(R.id.wheel_of_names_button);
        wheelOfNamesButton.setOnClickListener(v -> {
            startActivity(new Intent(FeaturesListActivity.this, WheelOfNamesActivity.class));
        });
    }
}