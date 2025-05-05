package com.android.ichooseyou.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.ichooseyou.fragment.HomeFragment;
import com.android.ichooseyou.fragment.ProfileFragment;
import com.android.ichooseyou.fragment.QuickRandomizeFragment;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class LandingPageActivity extends AppCompatActivity {
    public User user;

    // Cache to store fragment instances
    private final Map<Integer, Fragment> fragmentCache = new HashMap<>();

    // Constants for fragment tags
    private static final String HOME_FRAGMENT_TAG = "home_fragment";
    private static final String PROFILE_FRAGMENT_TAG = "profile_fragment";
    private static final String RANDOMIZE_FRAGMENT_TAG = "randomize_fragment";

    // Currently active fragment
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        user = getIntent().getParcelableExtra("LOGIN_USER");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            // Initialize fragments
            HomeFragment homeFragment = new HomeFragment();
            Bundle args = new Bundle();
            args.putParcelable("USER_DATA", user);
            homeFragment.setArguments(args);

            // Store in cache
            fragmentCache.put(R.id.navigation_home, homeFragment);

            // Set as active fragment
            activeFragment = homeFragment;

            // Add and show the fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment, HOME_FRAGMENT_TAG)
                    .commit();
        } else {
            // Restore fragments from savedInstanceState
            activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            // Rebuild fragment cache
            Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(HOME_FRAGMENT_TAG);
            if (homeFragment != null) fragmentCache.put(R.id.navigation_home, homeFragment);

            Fragment profileFragment = getSupportFragmentManager().findFragmentByTag(PROFILE_FRAGMENT_TAG);
            if (profileFragment != null) fragmentCache.put(R.id.navigation_profile, profileFragment);

            Fragment randomizeFragment = getSupportFragmentManager().findFragmentByTag(RANDOMIZE_FRAGMENT_TAG);
            if (randomizeFragment != null) fragmentCache.put(R.id.navigation_randomize, randomizeFragment);
        }
    }

    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                int itemId = item.getItemId();
                Fragment selectedFragment = getOrCreateFragment(itemId);

                if (selectedFragment != null && selectedFragment != activeFragment) {
                    // Hide current fragment and show the selected one
                    getSupportFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(selectedFragment)
                            .commit();

                    // Update active fragment
                    activeFragment = selectedFragment;
                }

                return true;
            };

    private Fragment getOrCreateFragment(int itemId) {
        // Check if fragment exists in cache
        if (fragmentCache.containsKey(itemId)) {
            return fragmentCache.get(itemId);
        }

        // Create new fragment if not in cache
        Fragment newFragment = null;
        String tag = null;

        if (itemId == R.id.navigation_home) {
            newFragment = new HomeFragment();
            tag = HOME_FRAGMENT_TAG;
        } else if (itemId == R.id.navigation_randomize) {
            newFragment = new QuickRandomizeFragment();
            tag = RANDOMIZE_FRAGMENT_TAG;
        } else if (itemId == R.id.navigation_profile) {
            newFragment = new ProfileFragment();
            tag = PROFILE_FRAGMENT_TAG;
        }

        if (newFragment != null) {
            // Set user data in arguments
            Bundle args = new Bundle();
            args.putParcelable("USER_DATA", user);
            newFragment.setArguments(args);

            // Add to cache
            fragmentCache.put(itemId, newFragment);

            // Add fragment but keep it hidden
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, newFragment, tag)
                    .hide(newFragment)
                    .commit();
        }

        return newFragment;
    }

    public void updateUser(User updatedUser) {
        this.user = updatedUser;

        // Update user data in all fragments
        for (Fragment fragment : fragmentCache.values()) {
            if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).updateUserData(user);
            } else if (fragment instanceof ProfileFragment) {
                ((ProfileFragment) fragment).updateUserData(user);
            } else if (fragment instanceof QuickRandomizeFragment) {
                Bundle args = new Bundle();
                args.putParcelable("USER_DATA", user);
                fragment.setArguments(args);
            }
        }
    }
}