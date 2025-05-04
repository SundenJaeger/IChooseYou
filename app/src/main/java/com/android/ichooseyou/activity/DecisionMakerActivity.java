package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DecisionMakerActivity extends AppCompatActivity {

    private TextInputEditText optionsEditText;
    private MaterialTextView resultTextView;
    private MaterialButton decideButton, clearButton, shuffleButton;
    private MaterialCardView resultCard;
    private RecyclerView optionsRecyclerView;
    private OptionsAdapter optionsAdapter;
    private List<String> optionsList = new ArrayList<>();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_maker);

        // Initialize views
        optionsEditText = findViewById(R.id.options_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
        decideButton = findViewById(R.id.decide_button);
        clearButton = findViewById(R.id.clear_button);
        shuffleButton = findViewById(R.id.shuffle_button);
        resultCard = findViewById(R.id.result_card);
        optionsRecyclerView = findViewById(R.id.options_recycler_view);

        // Setup RecyclerView
        optionsAdapter = new OptionsAdapter(optionsList);
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        optionsRecyclerView.setAdapter(optionsAdapter);

        // Set click listeners
        decideButton.setOnClickListener(v -> makeDecision());
        clearButton.setOnClickListener(v -> clearOptions());
        shuffleButton.setOnClickListener(v -> shuffleOptions());

        // Hide result card initially
        resultCard.setVisibility(View.GONE);
    }

    private void makeDecision() {
        String optionsText = optionsEditText.getText().toString().trim();

        if (optionsText.isEmpty()) {
            Toast.makeText(this, "Please enter some options", Toast.LENGTH_SHORT).show();
            return;
        }

        // Split and clean options
        String[] optionsArray = optionsText.split("\\n");
        optionsList.clear();
        for (String option : optionsArray) {
            if (!option.trim().isEmpty()) {
                optionsList.add(option.trim());
            }
        }

        if (optionsList.size() < 2) {
            Toast.makeText(this, "Please enter at least 2 options", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make decision
        String selectedOption = optionsList.get(random.nextInt(optionsList.size()));
        resultTextView.setText(selectedOption);
        resultCard.setVisibility(View.VISIBLE);

        // Update RecyclerView
        optionsAdapter.notifyDataSetChanged();
    }

    private void clearOptions() {
        optionsEditText.setText("");
        optionsList.clear();
        optionsAdapter.notifyDataSetChanged();
        resultCard.setVisibility(View.GONE);
    }

    private void shuffleOptions() {
        if (optionsList.size() > 1) {
            Collections.shuffle(optionsList);
            optionsAdapter.notifyDataSetChanged();

            // Update EditText with shuffled options
            StringBuilder sb = new StringBuilder();
            for (String option : optionsList) {
                sb.append(option).append("\n");
            }
            optionsEditText.setText(sb.toString().trim());
        }
    }
}