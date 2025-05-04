package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LotteryActivity extends AppCompatActivity {

    private RecyclerView numbersRecyclerView;
    private MaterialButton generateButton, saveButton, clearButton;
    private MaterialCardView resultCard;
    private LotteryNumberAdapter adapter;
    private List<Integer> currentNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);

        // Initialize views
        numbersRecyclerView = findViewById(R.id.numbers_recycler);
        generateButton = findViewById(R.id.generate_button);
        saveButton = findViewById(R.id.save_button);
        clearButton = findViewById(R.id.clear_button);
        resultCard = findViewById(R.id.result_card);

        // Setup RecyclerView
        adapter = new LotteryNumberAdapter(currentNumbers);
        numbersRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        numbersRecyclerView.setAdapter(adapter);

        // Set click listeners
        generateButton.setOnClickListener(v -> generateLotteryNumbers());
        saveButton.setOnClickListener(v -> saveNumbers());
        clearButton.setOnClickListener(v -> clearNumbers());

        // Hide result card initially
        resultCard.setVisibility(View.GONE);
    }

    private void generateLotteryNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 49; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        currentNumbers = numbers.subList(0, 6);
        Collections.sort(currentNumbers);

        // Update UI
        adapter.updateNumbers(currentNumbers);
        resultCard.setVisibility(View.VISIBLE);
        numbersRecyclerView.smoothScrollToPosition(0);

        // Special effect for jackpot numbers (all even or all odd)
        if (isJackpot(currentNumbers)) {
            Toast.makeText(this, "Jackpot Combination!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isJackpot(List<Integer> numbers) {
        boolean allEven = true;
        boolean allOdd = true;

        for (int num : numbers) {
            if (num % 2 == 0) {
                allOdd = false;
            } else {
                allEven = false;
            }
        }
        return allEven || allOdd;
    }

    private void saveNumbers() {
        if (currentNumbers.isEmpty()) {
            Toast.makeText(this, "Generate numbers first", Toast.LENGTH_SHORT).show();
            return;
        }
        // In a real app, save to database or shared preferences
        Toast.makeText(this, "Numbers saved!", Toast.LENGTH_SHORT).show();
    }

    private void clearNumbers() {
        currentNumbers.clear();
        adapter.updateNumbers(currentNumbers);
        resultCard.setVisibility(View.GONE);
        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }
}