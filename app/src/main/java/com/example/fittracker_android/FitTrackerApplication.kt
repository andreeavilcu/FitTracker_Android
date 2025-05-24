package com.example.fittracker_android

import android.app.Application
import com.example.fittracker_android.data.local.AppDatabase
import com.example.fittracker_android.data.repository.UserRepository

/**
 * Application class - runs when your app starts
 *
 * This is where we set up things that need to exist for the entire app lifetime:
 * - Database instance
 * - Repositories
 *
 * Think of it as the "setup" phase when your app launches
 */
class FitTrackerApplication : Application() {

    // Lazy means it's only created when first accessed
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }

    override fun onCreate() {
        super.onCreate()
        // Any app-wide initialization goes here
    }
}