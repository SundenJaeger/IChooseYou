package com.android.ichooseyou.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class ColorPickerActivity extends AppCompatActivity {

    private MaterialCardView colorCard;
    private MaterialTextView hexCodeText, rgbText;
    private MaterialButton generateButton, saveButton, copyButton;
    private int currentColor = Color.WHITE;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        // Initialize views
        colorCard = findViewById(R.id.color_card);
        hexCodeText = findViewById(R.id.hex_code_text);
        rgbText = findViewById(R.id.rgb_text);
        generateButton = findViewById(R.id.generate_button);
        saveButton = findViewById(R.id.save_button);
        copyButton = findViewById(R.id.copy_button);

        // Set initial color
        updateColorDisplay(currentColor);

        // Set click listeners
        generateButton.setOnClickListener(v -> generateRandomColor());

        saveButton.setOnClickListener(v -> {
            // In a real app, you would save to favorites/database
            Toast.makeText(this, "Color saved to favorites", Toast.LENGTH_SHORT).show();
        });

        copyButton.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip =
                    android.content.ClipData.newPlainText("Hex Color", hexCodeText.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Color code copied", Toast.LENGTH_SHORT).show();
        });

        // Long press to manually enter color
        colorCard.setOnLongClickListener(v -> {
            // Implement color picker dialog in a real app
            Toast.makeText(this, "Long press - implement color picker", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void generateRandomColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        currentColor = Color.rgb(red, green, blue);
        updateColorDisplay(currentColor);
    }

    private void updateColorDisplay(int color) {
        colorCard.setCardBackgroundColor(color);

        // Set text color based on color brightness
        int textColor = isColorDark(color) ? Color.WHITE : Color.BLACK;

        String hexCode = String.format("#%06X", (0xFFFFFF & color));
        hexCodeText.setText(hexCode);
        hexCodeText.setTextColor(textColor);

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        rgbText.setText(String.format("RGB: %d, %d, %d", red, green, blue));
        rgbText.setTextColor(textColor);
    }

    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) +
                0.587 * Color.green(color) +
                0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}