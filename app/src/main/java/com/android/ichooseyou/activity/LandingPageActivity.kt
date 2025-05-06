package com.android.ichooseyou.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.ichooseyou.fragment.HomeFragment
import com.android.ichooseyou.fragment.ProfileFragment
import com.android.ichooseyou.fragment.QuickRandomizeFragment
import com.android.ichooseyou.model.User
import com.android.ichooseyou2.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class LandingPageActivity : AppCompatActivity() {
    var user: User? = null

    // Cache to store fragment instances
    private val fragmentCache: MutableMap<Int, Fragment> = HashMap()

    // Currently active fragment
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // Get user data with backward compatibility
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("LOGIN_USER", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("LOGIN_USER")
        }

        // Set up bottom navigation with Kotlin lambda syntax
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            val itemId = item.itemId
            val selectedFragment = getOrCreateFragment(itemId)

            // Only proceed if fragment exists and is different from current one
            if (selectedFragment != null && selectedFragment !== activeFragment) {
                // Hide current fragment and show the selected one
                supportFragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(selectedFragment)
                    .commit()

                // Update active fragment
                activeFragment = selectedFragment
            }
            true
        }

        if (savedInstanceState == null) {
            // Initialize fragments
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("USER_DATA", user)
                }
            }

            // Store in cache
            fragmentCache[R.id.navigation_home] = homeFragment

            // Set as active fragment
            activeFragment = homeFragment

            // Add and show the fragment
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, homeFragment, HOME_FRAGMENT_TAG)
                .commit()
        } else {
            // Restore fragments from savedInstanceState
            supportFragmentManager.findFragmentById(R.id.fragment_container)?.let {
                activeFragment = it
            }

            // Rebuild fragment cache
            supportFragmentManager.findFragmentByTag(HOME_FRAGMENT_TAG)?.let {
                fragmentCache[R.id.navigation_home] = it
            }

            supportFragmentManager.findFragmentByTag(PROFILE_FRAGMENT_TAG)?.let {
                fragmentCache[R.id.navigation_profile] = it
            }

            supportFragmentManager.findFragmentByTag(RANDOMIZE_FRAGMENT_TAG)?.let {
                fragmentCache[R.id.navigation_randomize] = it
            }
        }
    }

    private fun getOrCreateFragment(itemId: Int): Fragment? {
        // Check if fragment exists in cache
        fragmentCache[itemId]?.let { return it }

        // Create new fragment if not in cache
        val newFragmentData = when (itemId) {
            R.id.navigation_home -> HomeFragment() to HOME_FRAGMENT_TAG
            R.id.navigation_randomize -> QuickRandomizeFragment() to RANDOMIZE_FRAGMENT_TAG
            R.id.navigation_profile -> ProfileFragment() to PROFILE_FRAGMENT_TAG
            else -> return null
        }

        val (newFragment, tag) = newFragmentData

        // Set user data in arguments
        newFragment.arguments = Bundle().apply {
            putParcelable("USER_DATA", user)
        }

        // Add to cache
        fragmentCache[itemId] = newFragment

        // Add fragment but keep it hidden
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, newFragment, tag)
            .hide(newFragment)
            .commit()

        return newFragment
    }

    fun updateUser(updatedUser: User) {
        user = updatedUser

        // Update user data in all fragments
        for (fragment in fragmentCache.values) {
            when (fragment) {
                is HomeFragment -> fragment.updateUserData(user!!)
                is ProfileFragment -> fragment.updateUserData(user)
                is QuickRandomizeFragment -> {
                    // Update arguments using property access instead of setArguments
                    fragment.arguments = Bundle().apply {
                        putParcelable("USER_DATA", user)
                    }
                }
            }
        }
    }

    companion object {
        // Constants for fragment tags
        private const val HOME_FRAGMENT_TAG = "home_fragment"
        private const val PROFILE_FRAGMENT_TAG = "profile_fragment"
        private const val RANDOMIZE_FRAGMENT_TAG = "randomize_fragment"
    }
}