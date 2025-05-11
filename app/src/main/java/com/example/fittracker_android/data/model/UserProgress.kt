package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Represents a user's fitness progress metrics recorded at a specific point in time.
 * This enables tracking changes in physical attributes and fitness capabilities over time.
 */
@Serializable
data class UserProgress(
    val id: String = UUID.randomUUID().toString(),

    val userId: String,

    val recordDate: Long = System.currentTimeMillis(),

    val weight: Float? = null,
    val bodyFatPercentage: Float? = null,

    val chest: Float? = null,
    val waist: Float? = null,
    val hips: Float? = null,
    val biceps: Float? = null,
    val thighs: Float? = null,

    val restingHeartRate: Int? = null,
    val vo2Max: Float? = null,

    val pushUpCount: Int? = null,
    val pullUpCount: Int? = null,
    val plankDuration: Int? = null,
    val runDistance: Float? = null,
    val runTime: Int? = null,

    val notes: String? = null,

    val progressPhotoUrl: String? = null
)