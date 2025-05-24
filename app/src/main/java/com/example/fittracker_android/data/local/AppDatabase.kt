package com.example.fittracker_android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fittracker_android.data.local.dao.*
import com.example.fittracker_android.data.local.entities.*

/**
 * Simplified database without foreign key constraints for initial setup
 */
@Database(
    entities = [
        UserEntity::class,
        ExerciseEntity::class,
        WorkoutEntity::class,
        WorkoutLogEntity::class,
        ExerciseLogEntity::class,
        ExerciseSetEntity::class,
        UserProgressEntity::class
    ],
    version = 2, // Increment version when changing schema
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun exerciseLogDao(): ExerciseLogDao
    abstract fun exerciseSetDao(): ExerciseSetDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_tracker_database"
                )
                    .fallbackToDestructiveMigration() // Will recreate DB on schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}