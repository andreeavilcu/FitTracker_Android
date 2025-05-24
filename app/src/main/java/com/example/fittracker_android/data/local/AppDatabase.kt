package com.example.fittracker_android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fittracker_android.data.local.dao.UserDao
import com.example.fittracker_android.data.local.entity.UserEntity

/**
 * The main database for the app
 *
 * @Database - Marks this as a Room database
 * entities - List of all tables in the database
 * version - Database version (increment when you change schema)
 *
 * This is like the "control center" for your database
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function to get the DAO
    abstract fun userDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is not null, return it
            // If it is, create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_tracker_database"
                )
                    .fallbackToDestructiveMigration() // Recreates database if version changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}