package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import java.util.Random;

public class DiceRollActivity extends AppCompatActivity {

    private ImageView diceImage;
    private TextView resultText;
    private Random random = new Random();
    private boolean isRolling = false;
    private int[] diceFaces = {
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);

        diceImage = findViewById(R.id.dice_image);
        resultText = findViewById(R.id.result_text);

        findViewById(R.id.roll_button).setOnClickListener(v -> {
            if (!isRolling) {
                rollDice();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void rollDice() {
        isRolling = true;
        resultText.setText("Rolling...");
        resultText.setTextColor(getResources().getColor(R.color.rolling_text_color));

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(300);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                final Runnable changeDice = new Runnable() {
                    @Override
                    public void run() {
                        if (isRolling) {
                            int tempRoll = random.nextInt(6);
                            diceImage.setImageResource(diceFaces[tempRoll]);
                            diceImage.postDelayed(this, 100);
                        }
                    }
                };
                diceImage.post(changeDice);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRolling = false;
                int roll = random.nextInt(6);
                diceImage.setImageResource(diceFaces[roll]);
                diceImage.startAnimation(fadeIn);
                resultText.setText(String.format("You rolled: %d", roll + 1));
                resultText.setTextColor(getResources().getColor(R.color.result_text_color));

                if (roll == 5) {
                    Animation celebrate = AnimationUtils.loadAnimation(DiceRollActivity.this, R.anim.bounce);
                    diceImage.startAnimation(celebrate);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        diceImage.startAnimation(shake);
    }
}