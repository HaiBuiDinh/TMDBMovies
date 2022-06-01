package com.example.tmdbmovies.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.ActivityMainBinding
import com.example.tmdbmovies.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //Navigation Component
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    //Splash Screen
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //need binding before setContentView on Activity
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                sharedViewModel.isLoading.value
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener{_, destination, _ ->
            when (destination.id) {
                R.id.searchMovieFragment -> hideBottomNavigation()
                R.id.searchTvFragment -> hideBottomNavigation()
                R.id.movieDetailFragment -> hideBottomNavigation()
                R.id.tvDetailFragment -> hideBottomNavigation()
                R.id.favoriteMovieFragment -> hideBottomNavigation()
                R.id.favoriteTvFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }

        //Bottom Navigation Controller setup
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieFragment,
                R.id.tvFragment,
                R.id.aboutFragment
            )
        )
        binding.bottNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = this.findNavController(R.id.mainNavHost)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showBottomNavigation() {
        binding.bottNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottNavigation.visibility = View.GONE
    }

}