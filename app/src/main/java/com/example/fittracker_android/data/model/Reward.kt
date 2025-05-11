package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents an achievement or reward that users can earn in the fitness app.
 * Part of the gamification system to increase user engagement and motivation.
 */
@Serializable
data class Reward(
    val id: String = UUID.randomUUID().toString(),

    val name: String,
    val description: String,
    val points: Int,
    val category: RewardCategory,

    val iconUrl: String,
    val level: RewardLevel = RewardLevel.BRONZE,

    val criteriaType: CriteriaType,
    val criteriaValue: Int,

    val isSecret: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis(),
    val dateEarned: Long? = null,

    val userId: String? = null
)

/**
 * Categories of rewards to organize different achievement types.
 */
@Serializable
enum class RewardCategory {
    WORKOUT_COMPLETION,
    CONSISTENCY,
    MILESTONE,
    PERSONAL_BEST,
    CHALLENGE,
    SOCIAL
}

/**
 * Tier levels for rewards, allowing for progression within categories.
 */
@Serializable
enum class RewardLevel {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND
}

/**
 * Types of criteria that can be used to earn rewards.
 */
@Serializable
enum class CriteriaType {
    WORKOUT_COUNT,
    EXERCISE_COUNT,
    CONSECUTIVE_DAYS,
    TOTAL_DISTANCE,
    TOTAL_WEIGHT_LIFTED,
    TOTAL_DURATION,
    SPECIFIC_EXERCISE_COUNT,
    CALORIES_BURNED,
    WEIGHT_LOSS_PERCENTAGE,
    PROFILE_COMPLETION
}