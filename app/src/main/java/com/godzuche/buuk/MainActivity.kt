package com.godzuche.buuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.view.Gravity
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.godzuche.buuk.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        setSupportActionBar(binding.appBarMain.toolbarMain)

        bottomNavigationView = binding.bottomNavView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> hideTopAppBarAndBottomNav()
                R.id.signUpFragment -> hideTopAppBarAndBottomNav()
                else -> showTopAppBarAndBottomNav()
            }
        }
    }

    private fun showTopAppBarAndBottomNav() {
//        TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
        binding.appBarMain.toolbarMain.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideTopAppBarAndBottomNav() {
        TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment, true))
        binding.appBarMain.toolbarMain.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
    }
}