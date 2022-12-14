package com.catness.blinkblinkbeach.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.catness.blinkblinkbeach.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value!!
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isUserAuthenticated.observe(this) { isUserAuthenticated ->
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            if (isUserAuthenticated == true) {
                navController.apply {
                    popBackStack()
                    navigate(R.id.homeFragment)
                }
            }
        }
    }
}