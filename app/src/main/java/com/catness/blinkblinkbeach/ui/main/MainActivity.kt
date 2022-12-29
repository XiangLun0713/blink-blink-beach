package com.catness.blinkblinkbeach.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.catness.blinkblinkbeach.NavGraphDirections
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.databinding.ActivityMainBinding
import com.catness.blinkblinkbeach.ui.eventList.EventListFragment
import com.catness.blinkblinkbeach.ui.home.HomeFragment
import com.catness.blinkblinkbeach.ui.notification.NotificationFragment
import com.catness.blinkblinkbeach.ui.profile.ProfileFragment
import com.catness.blinkblinkbeach.ui.report.ReportFragment
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
                R.id.profileFragment,
                R.id.eventDetailFragment
            )
        )

        binding.apply {
            setContentView(root)
            setSupportActionBar(topAppBar)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.apply {
            supportFragmentManager.registerFragmentLifecycleCallbacks(object :
                FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    v: View,
                    savedInstanceState: Bundle?,
                ) {
                    when (f) {
                        is HomeFragment, is NotificationFragment, is ReportFragment, is ProfileFragment, is EventListFragment -> {
                            topAppBar.isVisible = true
                            bottomNavigationMenu.isVisible = true
                        }
                        else -> {
                            topAppBar.isVisible = false
                            bottomNavigationMenu.isVisible = false
                        }
                    }
                }
            }, true)
        }


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

        // set up bottom nav bar
        NavigationUI.setupWithNavController(binding.bottomNavigationMenu, navController)
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