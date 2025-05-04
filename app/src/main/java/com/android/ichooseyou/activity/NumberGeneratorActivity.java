package com.android.ichooseyou.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class NumberGeneratorActivity extends AppCompatActivity {

    private TextInputEditText minInput, maxInput;
    private MaterialTextView resultText;
    private MaterialButton generateButton;
    private MaterialCardView resultCard;
    private ValueAnimator animator;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_generator);

        // Initialize views
        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.result_text);
        resultCard = findViewById(R.id.result_card);
        generateButton = findViewById(R.id.generate_button);

        // Set click listeners
        generateButton.setOnClickListener(v -> generateRandomNumber());
    }

    private void generateRandomNumber() {
        try {
            int min = Integer.parseInt(minInput.getText().toString());
            int max = Integer.parseInt(maxInput.getText().toString());

            if (min >= max) {
                Toast.makeText(this, "Max must be greater than min", Toast.LENGTH_SHORT).show();
                return;
            }

            if (min < 0 || max < 0) {
                Toast.makeText(this, "Please enter positive numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            generateButton.setEnabled(false);
            resultCard.setVisibility(View.VISIBLE);
            animateRandomNumber(min, max);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void animateRandomNumber(int min, int max) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        // Create animation that cycles through numbers
        animator = ValueAnimator.ofInt(min, max);
        animator.setDuration(700);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(3);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            resultText.setText(String.valueOf(value));
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int randomNumber = random.nextInt((max - min) + 1) + min;
                resultText.setText(String.valueOf(randomNumber));
                generateButton.setEnabled(true);
                celebrateResult(randomNumber);
            }
        });

        animator.start();
    }

    private void celebrateResult(int number) {
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        resultCard.startAnimation(pulse);

        if (isSpecialNumber(number)) {
            Toast.makeText(this, "Special Number!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSpecialNumber(int number) {
        // Check for prime numbers
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (animator != null) {
            animator.cancel();
        }
        super.onDestroy();
    }
}