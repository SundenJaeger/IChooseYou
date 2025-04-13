package com.android.ichooseyou.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import java.util.Random;

public class NumberGeneratorActivity extends AppCompatActivity {

    private EditText minInput, maxInput;
    private TextView resultText;
    private Button generateButton;
    private ImageView pokeballLogo;
    private Random random = new Random();
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_generator);

        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.result_text);
        generateButton = findViewById(R.id.generate_button);
        pokeballLogo = findViewById(R.id.pokeball_logo);

        generateButton.setOnClickListener(v -> generateRandomNumber());
        pokeballLogo.setOnClickListener(v -> spinPokeball());
    }

    private void generateRandomNumber() {
        try {
            int min = Integer.parseInt(minInput.getText().toString());
            int max = Integer.parseInt(maxInput.getText().toString());

            if (min >= max) {
                Toast.makeText(this, "Max must be greater than min", Toast.LENGTH_SHORT).show();
                return;
            }

            generateButton.setEnabled(false);
            animateRandomNumber(min, max);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void animateRandomNumber(int min, int max) {
        if (animator != null) {
            animator.cancel();
        }

        animator = ValueAnimator.ofInt(min, max);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

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
                celebrateResult();
            }
        });

        animator.start();
    }

    private void spinPokeball() {
        pokeballLogo.animate()
                .rotationBy(360)
                .setDuration(800)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    private void celebrateResult() {
        resultText.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(300)
                .withEndAction(() -> resultText.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start())
                .start();
    }

    @Override
    protected void onDestroy() {
        if (animator != null) {
            animator.cancel();
        }
        super.onDestroy();
    }
}