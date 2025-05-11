package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a workout session in the fitness tracking application.
 * Contains information about workout type, duration, exercises, and statistics.
 */
@Serializable
data class Workout(
    val id: String = UUID.randomUUID().toString(),

    val userId: String,
    val name: String,
    val type: WorkoutType,
    val timestamp: Long = System.currentTimeMillis(),

    val duration: Int,
    val caloriesBurned: Int? = null,
    val notes: String? = null,

    val isCompleted: Boolean = false,

    val exercises: List<String> = emptyList()
)

/**
 * Enumeration of possible workout types.
 */
@Serializable
enum class WorkoutType {
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    BALANCE,
    SPORT,
    HIIT,
    CIRCUIT,
    CUSTOM
}