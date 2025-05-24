package com.example.fittracker_android.data.model

import kotlinx.serialization.Serializable
import java.util.UUID
/**
 * Clasa principală User - reprezentarea completă a utilizatorului în aplicație
 * Aceasta este folosită în logica de business și UI
 */
@Serializable
data class User (
    val id: String = UUID.randomUUID().toString(),

    //informatii de autentificare
    val username: String,
    val email: String,

    //Informatii personale
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val gender: Gender? = null,

    // Informații fizice
    val height: Float? = null,
    val weight: Float? = null,

    // Preferințe fitness
    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityGoal: Int = 30, // minute pe zi
    val calorieGoal: Int? = null, // calorii pe zi
    val weeklyWorkoutGoal: Int = 3, // antrenamente pe săptămână

    val preferredUnits: Units = Units.METRIC,
    val notificationsEnabled: Boolean = true,
    val reminderTime: String? = null, // HH:mm format

    // Metadata
    val joinDate: Long = System.currentTimeMillis(),
    val lastActiveDate: Long = System.currentTimeMillis(),
    val profileImageUrl: String? = null,

    // Statistici calculate (readonly în UI)
    val totalWorkouts: Int = 0,
    val totalCaloriesBurned: Int = 0,
    val currentStreak: Int = 0, // zile consecutive cu activitate
    val longestStreak: Int = 0
)
{
    // Proprietăți calculate
    val fullName: String
        get() = when {
            firstName != null && lastName != null -> "$$firstName $lastName"
            firstName != null -> firstName
            lastName != null -> lastName
            else -> username
        }

    val displayName: String
        get() = firstName ?: username

    val bmi: Float?
        get() = if (height != null && weight != null && height > 0) {
            val heightInMeters = height / 100f
            weight / (heightInMeters * heightInMeters)
        } else null

    val bmiCategory: BMICategory?
        get() = bmi?.let { bmiValue ->
            when {
                bmiValue < 18.5f -> BMICategory.UNDERWEIGHT
                bmiValue < 25f -> BMICategory.NORMAL
                bmiValue < 30f -> BMICategory.OVERWEIGHT
                else -> BMICategory.OBESE
            }
        }

    val isProfileComplete: Boolean
        get() = firstName != null &&
                lastName != null &&
                age != null &&
                gender != null &&
                height != null &&
                weight != null

    val recommendedCalories: Int?
        get() = if (age != null && gender != null && height != null && weight != null) {
            calculateBMR()?.let { bmr ->
                // Aplicăm factorul de activitate bazat pe fitness level
                val activityFactor = when (fitnessLevel) {
                    FitnessLevel.BEGINNER -> 1.375f // ușor activ
                    FitnessLevel.INTERMEDIATE -> 1.55f // moderat activ
                    FitnessLevel.ADVANCED -> 1.725f // foarte activ
                    FitnessLevel.EXPERT -> 1.9f // extrem de activ
                }
                (bmr * activityFactor).toInt()
            }
        } else null

    fun isActive(): Boolean =
        System.currentTimeMillis() - lastActiveDate < 7 * 24 * 60 * 60 * 1000 // activ în ultimele 7 zile

    fun getDaysJoined(): Int =
        ((System.currentTimeMillis() - joinDate) / (24 * 60 * 60 * 1000)).toInt()

    fun getWeightInPreferredUnit(): Float? =
        weight?.let { w ->
            when (preferredUnits) {
                Units.METRIC -> w
                Units.IMPERIAL -> w * 2.20462f // kg to lbs
            }
        }
    fun getHeightInPreferredUnit(): Float? =
        height?.let { h ->
            when (preferredUnits) {
                Units.METRIC -> h
                Units.IMPERIAL -> h * 0.393701f // cm to inches
            }
        }

    fun needsWeightUpdate(): Boolean {
        // Sugerează actualizarea greutății dacă nu a fost actualizată în ultimele 30 de zile
        // Aceasta ar trebui implementată cu un timestamp separat pentru ultima actualizare a greutății
        return weight == null
    }

    private fun calculateBMR(): Float? {
        // Harris-Benedict Equation (Revised)
        return if (age != null && gender != null && height != null && weight != null) {
            when (gender) {
                Gender.MALE -> 88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * age)
                Gender.FEMALE -> 447.593f + (9.247f * weight) + (3.098f * height) - (4.330f * age)
                Gender.PREFER_NOT_TO_SAY -> {
                    // Folosim media dintre masculin și feminin
                    val male = 88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * age)
                    val female = 447.593f + (9.247f * weight) + (3.098f * height) - (4.330f * age)
                    (male + female) / 2f
                }
            }
        } else null
    }
}

/**
 * Sistem de unități de măsură
 */
@Serializable
enum class Units(val displayName: String) {
    METRIC("Metric (kg,cm)"),
    IMPERIAL("Imperial (lbs, ft/in)")
}

/**
 * Categorii BMI
 */

@Serializable
enum class BMICategory(val displayName: String, val description: String){
    UNDERWEIGHT("Underweight", "BMI below 18.5"),
    NORMAL("Normal weight", "BMI 18.5-24.9"),
    OVERWEIGHT("Overweight", "BMI 25-29.9"),
    OBESE("Obese", "BMI 30 or greater")
}

/**
 * Extension functions pentru validări
 */

fun User.validateForRegistration() : List<String> {
    val errors = mutableListOf<String>()

    if (email.isBlank()) errors.add("Email is required")
    if (!isValidEmail(email)) errors.add("Invalid email format")
    if (username.isBlank()) errors.add("Username is required")
    if (username.length < 3) errors.add("Username must be at least 3 characters")

    return errors
}

fun User.validatePhysicalData(): List<String> {
    val errors = mutableListOf<String>()

    height?.let { h ->
        if (h < 50 || h > 300) errors.add("Height must be between 50-300 cm")
    }

    weight?.let { w ->
        if (w < 20 || w > 500) errors.add("Weight must be between 20-500 kg")
    }

    age?.let { a ->
        if (a < 13 || a > 120) errors.add("Age must be between 13-120 years")
    }

    return errors
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


