package com.catness.blinkblinkbeach.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.catness.blinkblinkbeach.NavGraphDirections
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value!!
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        appBarConfiguration = AppBarConfiguration(
            // fragments placed inside this set will be consider as top-level destination
            setOf(
                R.id.homeFragment,
                R.id.reportFragment,
                R.id.notificationFragment,
                R.id.profileFragment
            )
        )

        binding.apply {
            setContentView(root)
            setSupportActionBar(topAppBar)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        viewModel.isUserAuthenticated.observe(this) { isUserAuthenticated ->
            if (isUserAuthenticated == true) {
                navController.apply {
                    popBackStack()
                    navigate(R.id.homeFragment)
                }
            }
        }

        // apply navigation on the action bar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // set up top app bar visibility listener
        viewModel.setVisibilityTopAppBar(navController, binding.topAppBar)

        // set up bottom nav bar
        NavigationUI.setupWithNavController(binding.bottomNavigationMenu, navController)

        // set up bottom nav visibility listener
        viewModel.setVisibilityBottomNavBar(navController, binding.bottomNavigationMenu)
    }

    override fun onSupportNavigateUp(): Boolean {
        // make the back button on action bar functional
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val action = NavGraphDirections.actionGlobalSignInFragment()
                viewModel.onLogOutButtonClick()
                navController.navigate(action)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}