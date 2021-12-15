package com.app.timerz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.timerz.R
import com.app.timerz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        val navController = navHostFragment.findNavController()

        val bottomNavigationView = binding.bottomNav

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (isTopLevelDestination(destination.id)) {
                bottomNavigationView.visibility = View.VISIBLE
                return@addOnDestinationChangedListener
            }

            bottomNavigationView.visibility = View.GONE
        }
    }

    private fun isTopLevelDestination(destination: Int): Boolean {
        val topLevelDestinations = getTopLevelDestinations()

        if (topLevelDestinations.contains(destination)) {
            return true
        }

        return false
    }

    private fun getTopLevelDestinations(): Set<Int> {
        return setOf(
            R.id.homeFragment,
            R.id.timerListFragment,
            R.id.settingsFragment
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}