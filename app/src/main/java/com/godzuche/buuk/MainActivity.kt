package com.godzuche.buuk

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.godzuche.buuk.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        installSplashScreen()

        setContentView(view)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)

        setSupportActionBar(binding.appBarMain.toolbarMain)

        bottomNavigationView = binding.bottomNavView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelDestinations = setOf(R.id.action_explore,
            R.id.action_favorites,
            R.id.action_learn,
            R.id.action_downloads,
            R.id.action_profile)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        binding.appBarMain.collapsingToolbarMain.setupWithNavController(binding.appBarMain.toolbarMain,
            navController,
            appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> hideTopAppBarAndBottomNav()
                R.id.signUpFragment -> hideTopAppBarAndBottomNav()
                R.id.emailLoginFragment -> hideTopAppBarAndBottomNav()
                else -> showTopAppBarAndBottomNav()
            }
        }
    }

    private fun showTopAppBarAndBottomNav() {
//        TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
        binding.appBarMain.collapsingToolbarMain.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideTopAppBarAndBottomNav() {
        TransitionManager.beginDelayedTransition(binding.root,
            Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
        binding.appBarMain.collapsingToolbarMain.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
    }

}