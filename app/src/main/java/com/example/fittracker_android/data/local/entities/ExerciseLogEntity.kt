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
    val exerciseId: String,

    val distance: Float? = null,
    val pace: Float? = null,
    val duration: Int? = null,

    val notes: String? = null,
    val orderInWorkout: Int = 0,

    val createdAt: Long = System.currentTimeMillis()
)