package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou2.R;

import java.util.Random;

public class CoinTossActivity extends AppCompatActivity {

    private ImageView coinImage;
    private TextView resultText;
    private Button backButton;
    private Random random = new Random();
    private boolean isTossing = false;
    private int lastSide = -1; // -1 = none, 0 = heads, 1 = tails

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        coinImage = findViewById(R.id.coin_image);
        resultText = findViewById(R.id.result_text);
        backButton = findViewById(R.id.coin_toss_back_button);

        findViewById(R.id.toss_button).setOnClickListener(v -> {
            if (!isTossing) {
                tossCoin();
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void tossCoin() {
        isTossing = true;
        resultText.setText("Tossing...");

        // Randomly choose heads or tails
        int side = random.nextInt(2); // 0 or 1

        // Create rotation animation
        RotateAnimation rotate = new RotateAnimation(0, 360 * 5,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1500);
        rotate.setFillAfter(true);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isTossing = false;
                lastSide = side;
                if (side == 0) {
                    coinImage.setImageResource(R.drawable.ic_coin_heads);
                    resultText.setText("Heads!");
                } else {
                    coinImage.setImageResource(R.drawable.ic_coin_tails);
                    resultText.setText("Tails!");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        coinImage.startAnimation(rotate);
    }
}