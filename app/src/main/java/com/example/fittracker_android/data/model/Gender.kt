package com.example.fittracker_android.data.model

/**
 * Enum representing gender options for user profiles
 * Includes a display name property for UI presentation
 */
enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    PREFER_NOT_TO_SAY("Prefer not to say")
}