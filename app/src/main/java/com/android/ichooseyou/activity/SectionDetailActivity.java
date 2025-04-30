package com.android.ichooseyou.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.ichooseyou2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class SectionDetailActivity extends AppCompatActivity {

    private TextView sectionTitleTextView;
    private ListView itemsListView;
    private Button editButton, removeButton, randomizeButton;
    private FloatingActionButton fabAddItem;

    private String sectionName;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_detail);

        // Get section name from intent
        sectionName = getIntent().getStringExtra("SECTION_NAME");
        if (TextUtils.isEmpty(sectionName)) {
            sectionName = "Unnamed Section";
        }

        // Initialize views
        sectionTitleTextView = findViewById(R.id.section_title);
        itemsListView = findViewById(R.id.items_list_view);
        editButton = findViewById(R.id.edit_button);
        removeButton = findViewById(R.id.remove_button);
        randomizeButton = findViewById(R.id.randomize_button);
        fabAddItem = findViewById(R.id.fab_add_item);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Set section title
        sectionTitleTextView.setText(sectionName);

        // Set up adapter for list view with custom item layout
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemsListView.setAdapter(adapter);

        // Set click listeners for buttons
        editButton.setOnClickListener(v -> showEditDialog());
        removeButton.setOnClickListener(v -> showRemoveConfirmationDialog());
        randomizeButton.setOnClickListener(v -> showRandomizeOptions());
        fabAddItem.setOnClickListener(v -> showAddItemDialog());

        // Set item click listener for list view
        itemsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showItemOptionsDialog(position);
            return true;
        });
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Item");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setHint("Enter item name");

        // Add padding to the input field
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, padding);
        container.addView(input);

        builder.setView(container);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String itemText = input.getText().toString().trim();
            if (!TextUtils.isEmpty(itemText)) {
                items.add(itemText);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Items");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setHint("Enter items (one per line)");

        // Pre-populate with existing items
        if (!items.isEmpty()) {
            input.setText(TextUtils.join("\n", items));
        }

        // Add padding to the input field
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, padding);
        container.addView(input);

        builder.setView(container);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String itemsText = input.getText().toString().trim();
            if (!TextUtils.isEmpty(itemsText)) {
                // Clear existing items
                items.clear();

                // Add new items
                String[] itemsArray = itemsText.split("\n");
                for (String item : itemsArray) {
                    if (!item.trim().isEmpty()) {
                        items.add(item.trim());
                    }
                }

                // Update the adapter
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Items updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showRemoveConfirmationDialog() {
        if (items.isEmpty()) {
            Toast.makeText(this, "No items to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Items");
        builder.setMessage("Are you sure you want to remove all items?");

        builder.setPositiveButton("Remove All", (dialog, which) -> {
            items.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "All items removed", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showRandomizeOptions() {
        if (items.isEmpty()) {
            Toast.makeText(this, "Please add some items first", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] options = {"List Picker", "Wheel of Names", "Team Picker"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Randomization Method");

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // List Picker
                    openListPicker();
                    break;
                case 1: // Wheel of Names
                    openWheelOfNames();
                    break;
                case 2: // Team Picker
                    openTeamPicker();
                    break;
            }
        });

        builder.show();
    }

    private void openListPicker() {
        Intent intent = new Intent(this, ListPickerActivity.class);
        intent.putStringArrayListExtra("ITEMS", items);
        startActivity(intent);
    }

    private void openWheelOfNames() {
        Intent intent = new Intent(this, WheelOfNamesActivity.class);
        intent.putStringArrayListExtra("ITEMS", items);
        startActivity(intent);
    }

    private void openTeamPicker() {
        Intent intent = new Intent(this, TeamPickerActivity.class);
        intent.putStringArrayListExtra("ITEMS", items);
        startActivity(intent);
    }

    private void showItemOptionsDialog(final int position) {
        String[] options = {"Edit", "Remove"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Item Options");

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Edit
                    showEditItemDialog(position);
                    break;
                case 1: // Remove
                    removeItem(position);
                    break;
            }
        });

        builder.show();
    }

    private void showEditItemDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item");

        // Set up the input field
        final EditText input = new EditText(this);
        input.setText(items.get(position));

        // Add padding to the input field
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(padding, padding, padding, padding);
        container.addView(input);

        builder.setView(container);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String itemText = input.getText().toString().trim();
            if (!TextUtils.isEmpty(itemText)) {
                items.set(position, itemText);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void removeItem(int position) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
    }
}