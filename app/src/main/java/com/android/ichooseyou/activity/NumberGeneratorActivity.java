package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import java.util.Random;

public class NumberGeneratorActivity extends AppCompatActivity {

    private EditText minInput, maxInput;
    private TextView resultText;
    private Button generateButton;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_generator);

        // Initialize views
        minInput = findViewById(R.id.min_input);
        maxInput = findViewById(R.id.max_input);
        resultText = findViewById(R.id.result_text);
        generateButton = findViewById(R.id.generate_button);

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

            int randomNumber = random.nextInt((max - min) + 1) + min;
            resultText.setText(String.valueOf(randomNumber));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}