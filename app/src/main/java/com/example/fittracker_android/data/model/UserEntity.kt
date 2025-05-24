package com.example.fittracker_android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fittracker_android.data.model.FitnessLevel
import com.example.fittracker_android.data.model.Gender

/**
 * Room Entity for User table
 *
 * @Entity - Tells Room this is a database table
 * @PrimaryKey - The unique identifier for each row
 *
 * This represents how user data is stored in the database
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String, // We'll store hashed passwords, never plain text!

    // Physical attributes (nullable because user might not set them initially)
    val height: Float? = null,
    val weight: Float? = null,
    val age: Int? = null,
    val gender: Gender? = null,

    // Fitness preferences
    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityGoal: Int = 30, // minutes per day
    val calorieGoal: Int? = null,

    // Account info
    val joinDate: Long = System.currentTimeMillis(),
    val profileImageUrl: String? = null,

    // Login state
    val isLoggedIn: Boolean = false
)