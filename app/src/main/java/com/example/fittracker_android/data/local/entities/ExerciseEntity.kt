package com.example.fittracker_android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fittracker_android.data.model.DifficultyLevel
import com.example.fittracker_android.data.model.Equipment
import com.example.fittracker_android.data.model.ExerciseType
import com.example.fittracker_android.data.model.MuscleGroup


/**
 * Room Entity for Exercise table
 */

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey
    val id: String,

    val name: String,
    val description: String,
    val instructions: String,
    val type: ExerciseType,

    // Store as comma-separated string for simplicity
    val muscleGroups: String,
    val difficulty: DifficultyLevel = DifficultyLevel.INTERMEDIATE,

    val equipmentRequired: String ="",

    val imageUrl: String? = null,
    val videoUrl: String? = null,

    val defaultSets: Int? = null,
    val defaultReps: Int? = null,
    val defaultDuration: Int? = null,
    val defaultDistance: Float? = null,
    val defaultWeight: Float? = null,

    val isCustom: Boolean = false,
    val createdBy: String? = null, // User ID if custom exercise
    val createdAt: Long = System.currentTimeMillis()

)