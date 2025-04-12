package com.android.ichooseyou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.ichooseyou.fragment.HomeFragment;
import com.android.ichooseyou.fragment.ProfileFragment;
import com.android.ichooseyou.fragment.QuickRandomizeFragment;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingPageActivity extends AppCompatActivity {
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        user = getIntent().getParcelableExtra("LOGIN_USER");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            Bundle args = new Bundle();

            args.putParcelable("USER_DATA",user);
            homeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
        }
    }
    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.navigation_randomize) {
                    // Replace QuickRandomizeFragment with FeaturesListActivity launch
                    Intent intent = new Intent(LandingPageActivity.this, FeaturesListActivity.class);
                    intent.putExtra("USER_DATA", user);
                    startActivity(intent);
                    return true; // Return true to indicate the event was handled
                } else if (itemId == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    Bundle args = new Bundle();
                    args.putParcelable("USER_DATA", user);
                    selectedFragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            };
}