package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ListPickerActivity extends AppCompatActivity {

    private TextInputEditText itemInput;
    private MaterialButton addButton, pickButton, backButton, clearButton, shuffleButton;
    private ListView itemsListView;
    private MaterialTextView resultText;
    private MaterialCardView resultCard;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_picker);

        // Initialize views
        itemInput = findViewById(R.id.item_input);
        addButton = findViewById(R.id.add_button);
        pickButton = findViewById(R.id.pick_button);
        clearButton = findViewById(R.id.clear_button);
        shuffleButton = findViewById(R.id.shuffle_button);
        itemsListView = findViewById(R.id.items_list);
        resultText = findViewById(R.id.result_text);
        resultCard = findViewById(R.id.result_card);
        backButton = findViewById(R.id.list_picker_back_button);

        // Check for passed items
        ArrayList<String> passedItems = getIntent().getStringArrayListExtra("ITEMS");
        if (passedItems != null) {
            items.addAll(passedItems);
        }

        // Set up list adapter with custom layout
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.item_text, items);
        itemsListView.setAdapter(adapter);

        // Set click listeners
        addButton.setOnClickListener(v -> addItem());
        pickButton.setOnClickListener(v -> pickRandomItem());
        clearButton.setOnClickListener(v -> clearList());
        shuffleButton.setOnClickListener(v -> shuffleList());
        backButton.setOnClickListener(v -> finish());

        // Set long click listener for item removal
        itemsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            removeItem(position);
            return true;
        });

        // Hide result card initially
        resultCard.setVisibility(View.GONE);
    }

    private void addItem() {
        String item = itemInput.getText().toString().trim();
        if (!item.isEmpty()) {
            items.add(item);
            adapter.notifyDataSetChanged();
            itemInput.setText("");
            resultCard.setVisibility(View.GONE);
        } else {
            showToast("Please enter an item");
        }
    }

    private void removeItem(int position) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        showToast("Item removed");
    }

    private void clearList() {
        if (!items.isEmpty()) {
            items.clear();
            adapter.notifyDataSetChanged();
            resultCard.setVisibility(View.GONE);
            showToast("List cleared");
        }
    }

    private void shuffleList() {
        if (items.size() > 1) {
            Collections.shuffle(items);
            adapter.notifyDataSetChanged();
            showToast("List shuffled");
        }
    }

    private void pickRandomItem() {
        if (items.isEmpty()) {
            showToast("Please add some items first");
            return;
        }

        resultCard.setVisibility(View.VISIBLE);
        int randomIndex = random.nextInt(items.size());
        String selectedItem = items.get(randomIndex);
        resultText.setText(selectedItem);

        // Highlight the selected item in the list
        itemsListView.setItemChecked(randomIndex, true);
        itemsListView.smoothScrollToPosition(randomIndex);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}