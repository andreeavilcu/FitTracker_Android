package com.example.fittracker_android

import android.app.Application
import com.example.fittracker_android.data.local.AppDatabase
import com.example.fittracker_android.data.repository.ExerciseRepository
import com.example.fittracker_android.data.repository.UserRepository
import com.example.fittracker_android.data.repository.UserProgressRepository
import com.example.fittracker_android.data.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application class for FitTracker
 * Sets up database and repositories for dependency injection
 */
class FitTrackerApplication : Application() {

    // Application-level coroutine scope
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Database instance
    val database by lazy { AppDatabase.getDatabase(this) }

    // Repositories
    val userRepository by lazy { UserRepository(database.userDao()) }

    val exerciseRepository by lazy { ExerciseRepository(database.exerciseDao()) }

    val workoutRepository by lazy {
        WorkoutRepository(
            database.workoutDao(),
            database.workoutLogDao(),
            database.exerciseLogDao(),
            database.exerciseSetDao()
        )
    }

    val userProgressRepository by lazy { UserProgressRepository(database.userProgressDao()) }

    override fun onCreate() {
        super.onCreate()

        // Initialize default data in proper coroutine scope
        initializeDefaultData()
    }

    private fun initializeDefaultData() {
        // Use application scope for initialization
        applicationScope.launch {
            try {
                // Populate default exercises if database is empty
                exerciseRepository.populateDefaultExercises()
            } catch (e: Exception) {
                // Log error in production app
                android.util.Log.e("FitTrackerApp", "Error initializing default data", e)
            }
        }
    }
}