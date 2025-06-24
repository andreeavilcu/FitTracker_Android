package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fittracker_android.data.model.Mood

/**
 * Room Entity for WorkoutLog table - tracks completed workouts
 */
@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey
    val id: String,

    val userId: String,
    val workoutId: String?,

    val startTime: Long,
    val endTime: Long,
    val duration: Int,

    val caloriesBurned: Int? = null,

    val perceivedEffort: Int? = null,
    val mood: Mood? = null,
    val notes: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val heartRateAvg: Int? = null,
    val heartRateMax: Int? = null,

    val createdAt: Long = System.currentTimeMillis()
)