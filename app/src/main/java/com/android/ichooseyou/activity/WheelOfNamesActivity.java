package com.android.ichooseyou.activity;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import com.android.ichooseyou.model.WheelView;
import java.util.ArrayList;
import java.util.Random;

public class WheelOfNamesActivity extends AppCompatActivity {

    private EditText nameInput;
    private Button addNameButton, spinButton;
    private WheelView wheelView;
    private ArrayList<String> names = new ArrayList<>();
    private Random random = new Random();
    private boolean isSpinning = false;
    private ValueAnimator wheelAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_of_names);

        nameInput = findViewById(R.id.name_input);
        addNameButton = findViewById(R.id.add_name_button);
        spinButton = findViewById(R.id.spin_button);
        wheelView = findViewById(R.id.wheel_view);

        addNameButton.setOnClickListener(v -> addName());
        spinButton.setOnClickListener(v -> spinWheel());
    }

    private void addName() {
        String name = nameInput.getText().toString().trim();
        if (!name.isEmpty()) {
            names.add(name);
            nameInput.setText("");
            wheelView.setItems(names);
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    private void spinWheel() {
        if (names.isEmpty()) {
            Toast.makeText(this, "Please add some names first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isSpinning) return;

        isSpinning = true;
        final int selectedIndex = random.nextInt(names.size());

        // Calculate angle needed to position selected segment at arrow (top center)
        float anglePerSegment = 360f / names.size();
        float segmentCenterAngle = selectedIndex * anglePerSegment + anglePerSegment/2;
        float arrowPositionAngle = 270f; // Arrow points to top (270° in mathematical coordinates)
        float rotations = 7f; // Number of full rotations

        // Calculate final angle to position selected segment under arrow
        float finalAngle = rotations * 360f + (arrowPositionAngle - segmentCenterAngle);

        // Make the spin longer (6 seconds)
        wheelAnimator = ValueAnimator.ofFloat(0, finalAngle);
        wheelAnimator.setDuration(6000); // 6 seconds

        // Custom interpolator for smooth deceleration
        wheelAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input < 0.8f) {
                    // First 80% - constant speed
                    return input;
                } else {
                    // Last 20% - ease-out deceleration
                    float t = (input - 0.8f) / 0.2f;
                    return 0.8f + (1 - (1 - t) * (1 - t)) * 0.2f;
                }
            }
        });

        wheelAnimator.addUpdateListener(animation -> {
            wheelView.setRotationAngle((float) animation.getAnimatedValue());
        });

        wheelAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override public void onAnimationStart(android.animation.Animator animation) {}
            @Override public void onAnimationCancel(android.animation.Animator animation) {}
            @Override public void onAnimationRepeat(android.animation.Animator animation) {}

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isSpinning = false;
                wheelView.setSelectedIndex(selectedIndex);
                showWinnerPopup(names.get(selectedIndex));
            }
        });

        wheelAnimator.start();
    }

    private void showWinnerPopup(String winnerName) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_winner, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView winnerText = dialogView.findViewById(R.id.winner_name);
        winnerText.setText(winnerName);

        Button closeButton = dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (wheelAnimator != null) {
            wheelAnimator.cancel();
        }
        super.onDestroy();
    }
}