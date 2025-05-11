package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a completed workout session with detailed performance metrics.
 * This is used to track what the user actually did during a workout,
 * as opposed to what was planned.
 */
@Serializable
data class WorkoutLog(
    val id: String = UUID.randomUUID().toString(),

    val userId: String,
    val workoutId: String,

    val startTime: Long,
    val endTime: Long,
    val duration: Int,

    val caloriesBurned: Int? = null,
    val exerciseLogs: List<ExerciseLog> = emptyList(),

    val perceivedEffort: Int? = null,
    val mood: Mood? = null,
    val notes: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,

    val heartRateAvg: Int? = null,
    val heartRateMax: Int? = null
)

/**
 * Represents the performance details of a single exercise within a workout.
 */
@Serializable
data class ExerciseLog(
    val id: String = UUID.randomUUID().toString(),
    val exerciseId: String,
    val sets: List<ExerciseSet> = emptyList(),

    val distance: Float? = null,
    val pace: Float? = null,
    val duration: Int? = null,

    val notes: String? = null
)

/**
 * Represents a single set of a resistance/strength exercise.
 */
@Serializable
data class ExerciseSet(
    val reps: Int? = null,
    val weight: Float? = null,
    val duration: Int? = null,
    val restTime: Int? = null,
    val setNumber: Int,
    val isWarmupSet: Boolean = false
)

/**
 * User's mood during workout, useful for analysis.
 */
@Serializable
enum class Mood {
    GREAT,
    GOOD,
    AVERAGE,
    TIRED,
    EXHAUSTED
}