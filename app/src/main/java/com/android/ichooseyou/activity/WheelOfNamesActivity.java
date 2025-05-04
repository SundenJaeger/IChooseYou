package com.android.ichooseyou.activity;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ichooseyou2.R;
import com.android.ichooseyou.model.WheelView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Random;

public class WheelOfNamesActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputLayout nameInputLayout;
    private MaterialButton addNameButton, spinButton, clearNamesButton;
    private WheelView wheelView;
    private TextView namesCountText;
    private RecyclerView namesRecyclerView;
    private NamesAdapter namesAdapter;
    private ArrayList<String> names = new ArrayList<>();
    private Random random = new Random();
    private boolean isSpinning = false;
    private ValueAnimator wheelAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_of_names);

        // Initialize views
        nameInput = findViewById(R.id.name_input);
        nameInputLayout = findViewById(R.id.name_input_layout);
        addNameButton = findViewById(R.id.add_name_button);
        spinButton = findViewById(R.id.spin_button);
        clearNamesButton = findViewById(R.id.clear_names_button);
        wheelView = findViewById(R.id.wheel_view);
        namesCountText = findViewById(R.id.names_count_text);
        namesRecyclerView = findViewById(R.id.names_recycler_view);

        // Set up RecyclerView
        namesAdapter = new NamesAdapter();
        namesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        namesRecyclerView.setAdapter(namesAdapter);
        namesRecyclerView.setHasFixedSize(true);

        // Set up toolbar navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Check if items were passed from SectionDetailActivity
        ArrayList<String> passedItems = getIntent().getStringArrayListExtra("ITEMS");
        if (passedItems != null && !passedItems.isEmpty()) {
            names.addAll(passedItems);
            updateWheelAndCount();
        }

        // Set up input field behavior
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addNameButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        nameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addName();
                return true;
            }
            return false;
        });

        // Set up button click listeners
        addNameButton.setOnClickListener(v -> addName());
        spinButton.setOnClickListener(v -> spinWheel());
        clearNamesButton.setOnClickListener(v -> {
            if (names.isEmpty()) {
                Toast.makeText(this, "The list is already empty", Toast.LENGTH_SHORT).show();
            } else {
                names.clear();
                updateWheelAndCount();
                Toast.makeText(this, "All names removed", Toast.LENGTH_SHORT).show();
            }
        });

        // Update initial state
        updateWheelAndCount();
        addNameButton.setEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addName() {
        String input = nameInput.getText().toString().trim();
        if (!input.isEmpty()) {
            // Split by new lines and filter out empty lines
            String[] lines = input.split("\\r?\\n");
            boolean addedAny = false;

            for (String name : lines) {
                name = name.trim();
                if (!name.isEmpty()) {
                    names.add(name);
                    addedAny = true;
                }
            }

            if (addedAny) {
                nameInput.setText("");
                updateWheelAndCount();
                nameInputLayout.requestFocus();
                namesAdapter.notifyDataSetChanged();
                namesRecyclerView.smoothScrollToPosition(names.size() - 1);
            } else {
                nameInputLayout.setError("Please enter valid names");
            }
        } else {
            nameInputLayout.setError("Please enter names");
        }
    }

    private void updateWheelAndCount() {
        wheelView.setItems(names);
        namesCountText.setText(getResources().getQuantityString(
                R.plurals.names_count, names.size(), names.size()));
        spinButton.setEnabled(names.size() > 1);
        namesAdapter.notifyDataSetChanged();
    }

    private void spinWheel() {
        if (names.size() < 2) {
            Toast.makeText(this, "Please add at least 2 names", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isSpinning) return;

        isSpinning = true;
        final int selectedIndex = random.nextInt(names.size());

        float anglePerSegment = 360f / names.size();
        float segmentCenterAngle = selectedIndex * anglePerSegment + anglePerSegment / 2;
        float arrowPositionAngle = 270f;
        float rotations = 7f;
        float finalAngle = rotations * 360f + (arrowPositionAngle - segmentCenterAngle);

        wheelAnimator = ValueAnimator.ofFloat(0, finalAngle);
        wheelAnimator.setDuration(6000);

        wheelAnimator.setInterpolator(input -> {
            if (input < 0.8f) return input;
            float t = (input - 0.8f) / 0.2f;
            return 0.8f + (1 - (1 - t) * (1 - t)) * 0.2f;
        });

        wheelAnimator.addUpdateListener(animation ->
                wheelView.setRotationAngle((float) animation.getAnimatedValue()));

        wheelAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                spinButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                isSpinning = false;
                spinButton.setEnabled(names.size() > 1);
                wheelView.setSelectedIndex(selectedIndex);
                showWinnerDialog(names.get(selectedIndex));
            }

            @Override public void onAnimationCancel(android.animation.Animator animation) {}
            @Override public void onAnimationRepeat(android.animation.Animator animation) {}
        });

        wheelAnimator.start();
    }

    private void showWinnerDialog(String winnerName) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_winner, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView winnerText = dialogView.findViewById(R.id.winner_name);
        winnerText.setText(winnerName);

        MaterialButton closeButton = dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.NameViewHolder> {

        @NonNull
        @Override
        public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_name, parent, false);
            return new NameViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
            holder.nameText.setText(names.get(position));
            holder.deleteButton.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    names.remove(adapterPosition);
                    updateWheelAndCount();
                    notifyItemRemoved(adapterPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        class NameViewHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            MaterialButton deleteButton;

            NameViewHolder(View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.name_text);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (wheelAnimator != null) {
            wheelAnimator.cancel();
        }
        super.onDestroy();
    }
}