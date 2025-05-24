package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room Entity for ExerciseLog table - tracks individual exercises within a workout
 * Note: Removed foreign key to ExerciseEntity to avoid circular dependencies
 */
@Entity(
    tableName = "exercise_logs",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseLogEntity(
    @PrimaryKey
    val id: String,

    val workoutLogId: String,
    val exerciseId: String, // Just store as String, no foreign key constraint

    // For cardio exercises
    val distance: Float? = null,
    val pace: Float? = null, // minutes per km/mile
    val duration: Int? = null, // in seconds

    val notes: String? = null,
    val orderInWorkout: Int = 0, // Order of exercise in workout

    val createdAt: Long = System.currentTimeMillis()
)