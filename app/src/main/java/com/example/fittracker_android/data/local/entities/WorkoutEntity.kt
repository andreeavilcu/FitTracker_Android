package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fittracker_android.data.model.WorkoutType

/**
 * Room Entity for Workout table
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey
    val id: String,

    val userId: String,
    val name: String,
    val type: WorkoutType,
    val timestamp: Long = System.currentTimeMillis(),

    val duration: Int, // in minutes
    val caloriesBurned: Int? = null,
    val notes: String? = null,

    val isCompleted: Boolean = false,
    val isTemplate: Boolean = false, // Can be saved as template for future use

    // Store exercise IDs as comma-separated string
    val exercises: String = "", // "exercise1,exercise2,exercise3"

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)