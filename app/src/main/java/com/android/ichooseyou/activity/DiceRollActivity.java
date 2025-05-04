package com.android.ichooseyou.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.Random;

public class DiceRollActivity extends AppCompatActivity {

    private ImageView diceImage;
    private TextView resultText, historyText;
    private MaterialButton rollButton, backButton;
    private MaterialCardView resultCard;
    private Random random = new Random();
    private boolean isRolling = false;
    private int rollCount = 0;
    private int[] diceFaces = {
            R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3,
            R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);

        // Initialize views
        diceImage = findViewById(R.id.dice_image);
        resultText = findViewById(R.id.result_text);
        historyText = findViewById(R.id.history_text);
        rollButton = findViewById(R.id.roll_button);
        backButton = findViewById(R.id.dice_roll_back_button);
        resultCard = findViewById(R.id.result_card);

        // Initialize roll count to 0
        rollCount = 0;

        // Set click listeners
        rollButton.setOnClickListener(v -> {
            if (!isRolling) {
                rollDice();
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void rollDice() {
        if (isRolling) return; // Prevent multiple rolls

        isRolling = true;
        rollButton.setEnabled(false);
        resultText.setText("Rolling...");
        resultText.setTextColor(getResources().getColor(R.color.rolling_text_color));
        resultCard.setVisibility(View.VISIBLE);

        // Generate ONE random roll
        final int roll = random.nextInt(6);

        // Load animations
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(300);

        // Temporary dice face animation
        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            // Only show random faces during animation (not the actual result)
            int tempRoll = random.nextInt(6);
            if (tempRoll == roll) {
                tempRoll = (tempRoll + 1) % 6; // Ensure we don't show the result early
            }
            diceImage.setImageResource(diceFaces[tempRoll]);
        });

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animator.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Stop animations
                if (animator.isRunning()) {
                    animator.cancel();
                }

                // Show final result
                diceImage.setImageResource(diceFaces[roll]);
                diceImage.startAnimation(fadeIn);

                // Update UI - ONLY ONCE
                rollCount++;
                String result = String.format("Roll #%d: %d", rollCount, roll + 1);
                resultText.setText(result);
                resultText.setTextColor(getResources().getColor(R.color.result_text_color));

                // Update history - ONLY ONCE
                String currentHistory = historyText.getText().toString();
                historyText.setText(result + (currentHistory.isEmpty() ? "" : "\n" + currentHistory));

                // Special effects for 6
                if (roll == 5) {
                    Animation celebrate = AnimationUtils.loadAnimation(DiceRollActivity.this, R.anim.bounce);
                    diceImage.startAnimation(celebrate);
                }

                isRolling = false;
                rollButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        diceImage.clearAnimation();
        diceImage.startAnimation(shake);
    }
}