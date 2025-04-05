package com.android.ichooseyou.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialButton goBackButton = findViewById(R.id.go_back_button);
        MaterialButton aboutUsButton = findViewById(R.id.about_us_button);

        aboutUsButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, DevPageActivity.class);
            startActivity(intent);
        });
        goBackButton.setOnClickListener(e -> finish());
    }
}