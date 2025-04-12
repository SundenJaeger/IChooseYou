package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import java.util.ArrayList;
import java.util.Random;

public class ListPickerActivity extends AppCompatActivity {

    private EditText itemInput;
    private Button addButton, pickButton;
    private ListView itemsListView;
    private TextView resultText;
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
        itemsListView = findViewById(R.id.items_list);
        resultText = findViewById(R.id.result_text);

        // Set up list adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemsListView.setAdapter(adapter);

        // Set click listeners
        addButton.setOnClickListener(v -> addItem());
        pickButton.setOnClickListener(v -> pickRandomItem());

        // Set long click listener for item removal
        itemsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            items.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void addItem() {
        String item = itemInput.getText().toString().trim();
        if (!item.isEmpty()) {
            items.add(item);
            adapter.notifyDataSetChanged();
            itemInput.setText("");
        } else {
            Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickRandomItem() {
        if (items.isEmpty()) {
            Toast.makeText(this, "Please add some items first", Toast.LENGTH_SHORT).show();
            return;
        }

        int randomIndex = random.nextInt(items.size());
        String selectedItem = items.get(randomIndex);
        resultText.setText("Selected: " + selectedItem);
    }
}