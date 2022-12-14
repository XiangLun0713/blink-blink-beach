package com.catness.blinkblinkbeach.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.catness.blinkblinkbeach.R
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    true
                }
                R.id.report -> {
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    true
                }
                R.id.notification -> {
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.action_signInFragment_to_homeFragment)
                    true
                }
                else -> false
            }
        }
    }
}