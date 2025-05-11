package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents an exercise in the fitness tracking application.
 * Contains information about the exercise type, instructions, muscle groups involved,
 * and performance metrics.
 */
@Serializable
data class Exercise(
    val id: String = UUID.randomUUID().toString(),

    val name: String,
    val description: String,
    val instructions: String,
    val type: ExerciseType,

    val muscleGroups: List<MuscleGroup>,
    val difficulty: DifficultyLevel = DifficultyLevel.INTERMEDIATE,

    val equipmentRequired: List<Equipment> = emptyList(),

    val imageUrl: String? = null,
    val videoUrl: String? = null,

    val defaultSets: Int? = null,
    val defaultReps: Int? = null,
    val defaultDuration: Int? = null,
    val defaultDistance: Float? = null,
    val defaultWeight: Float? = null,

    val isCustom: Boolean = false
)

/**
 * Types of exercises based on their nature and execution.
 */
@Serializable
enum class ExerciseType {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    BALANCE,
    PLYOMETRIC,
    CALISTHENICS,
    SPORT_SPECIFIC
}

/**
 * Difficulty levels for exercises.
 */
@Serializable
enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

/**
 * Major muscle groups targeted by exercises.
 */
@Serializable
enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    FOREARMS,
    QUADRICEPS,
    HAMSTRINGS,
    CALVES,
    GLUTES,
    ABS,
    OBLIQUES,
    LOWER_BACK,
    FULL_BODY,
    CARDIO_RESPIRATORY
}

/**
 * Common exercise equipment.
 */
@Serializable
enum class Equipment {
    NONE,
    DUMBBELLS,
    BARBELL,
    KETTLEBELL,
    RESISTANCE_BANDS,
    MEDICINE_BALL,
    YOGA_MAT,
    BENCH,
    PULL_UP_BAR,
    TREADMILL,
    EXERCISE_BIKE,
    ELLIPTICAL,
    ROWING_MACHINE,
    CABLE_MACHINE,
    SMITH_MACHINE,
    TRX_SUSPENSION
}