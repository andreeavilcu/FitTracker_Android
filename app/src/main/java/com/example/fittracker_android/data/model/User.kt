package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

/**
 * Represents a user in the fitness tracking application.
 * Contains personal information and fitness-related attributes.
 */
@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),

    val username: String,
    val email: String,

    val height: Float? = null,
    val weight: Float? = null,
    val age: Int? = null,
    val gender: Gender? = null,

    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityGoal: Int = 30,
    val calorieGoal: Int? = null,

    val joinDate: Long = System.currentTimeMillis(),
    val profileImageUrl: String? = null
)

/**
 * Enumeration of possible fitness levels for a user.
 */
@Serializable
enum class FitnessLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

/**
 * Enumeration for user gender options.
 */
@Serializable
enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    PREFER_NOT_TO_SAY
}