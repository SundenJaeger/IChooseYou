package com.android.ichooseyou.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

public class DevPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_page);

        MaterialButton goBackButton = findViewById(R.id.go_back_button_dev);

        goBackButton.setOnClickListener(e -> finish());
    }
}