package com.catness.blinkblinkbeach.ui.main

import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.catness.blinkblinkbeach.R
import com.catness.blinkblinkbeach.data.repositories.auth.AuthRepository
import com.catness.blinkblinkbeach.data.repositories.main.MainRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isUserAuthenticated = MutableLiveData<Boolean>()
    val isUserAuthenticated: LiveData<Boolean> = _isUserAuthenticated

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            val isAuthenticated = repository.checkIfUserIsAuthenticated()
            _isUserAuthenticated.value = isAuthenticated
            _isLoading.value = false
        }
    }

    fun onLogOutButtonClick() = viewModelScope.launch {
        authRepository.signOut()
    }

    fun setVisibilityBottomNavBar(navController: NavController, bottomNavBar: BottomNavigationView) {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.homeFragment -> bottomNavBar.isVisible = true
                R.id.notificationFragment -> bottomNavBar.isVisible = true
                R.id.reportFragment -> bottomNavBar.isVisible = true
                R.id.profileFragment -> bottomNavBar.isVisible = true
                else -> bottomNavBar.isVisible = false
            }
        }

    }

    fun setVisibilityTopAppBar(navController: NavController, topAppBar: MaterialToolbar) {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.homeFragment -> topAppBar.isVisible = true
                R.id.notificationFragment -> topAppBar.isVisible = true
                R.id.reportFragment -> topAppBar.isVisible = true
                R.id.profileFragment -> topAppBar.isVisible = true
                else -> topAppBar.isVisible = false
            }
        }
    }
}