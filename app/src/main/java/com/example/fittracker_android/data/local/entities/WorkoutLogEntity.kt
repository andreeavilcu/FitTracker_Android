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
    val duration: Int, // in minutes

    val caloriesBurned: Int? = null,

    val perceivedEffort: Int? = null, // 1-10 scale
    val mood: Mood? = null,
    val notes: String? = null,

    // Location data
    val latitude: Double? = null,
    val longitude: Double? = null,

    // Heart rate data
    val heartRateAvg: Int? = null,
    val heartRateMax: Int? = null,

    val createdAt: Long = System.currentTimeMillis()
)