package com.example.fittracker_android.data.local.entities

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
    val passwordHash: String,



    val height: Float? = null,
    val weight: Float? = null,
    val age: Int? = null,
    val gender: Gender? = null,

    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityGoal: Int = 30,
    val calorieGoal: Int? = null,

    val joinDate: Long = System.currentTimeMillis(),
    val profileImageUrl: String? = null,

    val isLoggedIn: Boolean = false
)