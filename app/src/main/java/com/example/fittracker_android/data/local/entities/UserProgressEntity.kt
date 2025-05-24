package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Room Entity for UserProgress table - tracks user's fitness progress over time
 */
@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: String,

    val userId: String,
    val recordDate: Long = System.currentTimeMillis(),

    // Body measurements
    val weight: Float? = null, // in kg or lbs
    val bodyFatPercentage: Float? = null,

    // Body measurements in cm or inches
    val chest: Float? = null,
    val waist: Float? = null,
    val hips: Float? = null,
    val biceps: Float? = null,
    val thighs: Float? = null,

    // Fitness metrics
    val restingHeartRate: Int? = null,
    val vo2Max: Float? = null,

    // Performance tests
    val pushUpCount: Int? = null,
    val pullUpCount: Int? = null,
    val plankDuration: Int? = null, // in seconds
    val runDistance: Float? = null, // in km or miles
    val runTime: Int? = null, // in seconds

    val notes: String? = null,
    val progressPhotoUrl: String? = null,

    val createdAt: Long = System.currentTimeMillis()
)