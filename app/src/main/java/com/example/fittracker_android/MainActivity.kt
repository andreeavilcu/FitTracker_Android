package com.example.fittracker_android

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fittracker_android.data.local.entities.UserEntity
import com.example.fittracker_android.data.model.FitnessLevel
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.auth_nav_graph)
        navController.graph = navGraph

        createTestUserIfNeeded()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentDestination?.id
                when (currentDestination) {
                    R.id.loginFragment -> {
                        finish()
                    }
                    R.id.registerStep1Fragment, R.id.registerStep2Fragment -> {
                        navController.navigate(R.id.loginFragment)
                    }
                    R.id.profileFragment -> {
                        finish()
                    }
                    else -> {
                        navController.navigateUp()
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun createTestUserIfNeeded() {
        val app = application as FitTrackerApplication

        lifecycleScope.launch {
            val existingUser = app.database.userDao().getUserByEmail("test@test.com")
            if (existingUser == null) {
                val testUser = UserEntity(
                    id = UUID.randomUUID().toString(),
                    username = "Test User",
                    email = "test@test.com",
                    passwordHash = hashPassword("password"),
                    age = 25,
                    fitnessLevel = FitnessLevel.BEGINNER,

                    isLoggedIn = false
                )
                app.database.userDao().insertUser(testUser)

                android.util.Log.d("MainActivity", "Test user created: test@test.com / password")
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}