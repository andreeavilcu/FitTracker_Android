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
    val progressPhotoUrl: String? = null,

    val createdAt: Long = System.currentTimeMillis()
)