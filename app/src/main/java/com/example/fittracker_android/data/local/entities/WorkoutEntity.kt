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

    val duration: Int,
    val caloriesBurned: Int? = null,
    val notes: String? = null,

    val isCompleted: Boolean = false,
    val isTemplate: Boolean = false,


    val exercises: String = "",

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)