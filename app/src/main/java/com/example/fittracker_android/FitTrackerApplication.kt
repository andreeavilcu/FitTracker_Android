package com.example.fittracker_android

import android.app.Application
import com.example.fittracker_android.data.local.AppDatabase
import com.example.fittracker_android.data.local.UserPreferencesManager
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

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getDatabase(this) }

    val userRepository by lazy { UserRepository(database.userDao()) }

    val userPreferencesManager by lazy { UserPreferencesManager(this) }

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

        initializeDefaultData()
    }

    private fun initializeDefaultData() {
        applicationScope.launch {
            try {
                exerciseRepository.populateDefaultExercises()
            } catch (e: Exception) {
                android.util.Log.e("FitTrackerApp", "Error initializing default data", e)
            }
        }
    }
}