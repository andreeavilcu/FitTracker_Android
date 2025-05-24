package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room Entity for ExerciseSet table - tracks individual sets of strength exercises
 */
@Entity(
    tableName = "exercise_sets",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseLogId"],
            onDelete = ForeignKey.CASCADE

        )
    ]
)
data class ExerciseSetEntity(
    @PrimaryKey
    val id: String,

    val exerciseLogId: String,

    val reps: Int? = null,
    val weight: Float? = null, // in kg or lbs
    val duration: Int? = null, // in seconds (for time-based exercises)
    val restTime: Int? = null, // in seconds
    val setNumber: Int,
    val isWarmupSet: Boolean = false,
    val isCompleted: Boolean = true,

    val createdAt: Long = System.currentTimeMillis()
)