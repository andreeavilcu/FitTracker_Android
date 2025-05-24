package com.example.fittracker_android.data.model

/**
 * Enum representing fitness levels for user profiles
 * Includes a display name property for UI presentation
 */
enum class FitnessLevel(val displayName: String){
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced"),
    EXPERT("Expert");
}