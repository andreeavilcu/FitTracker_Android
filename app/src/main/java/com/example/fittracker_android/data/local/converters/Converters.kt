package com.example.fittracker_android.data.local

import androidx.room.TypeConverter
import com.example.fittracker_android.data.model.*

/**
 * Extended Type Converters for Room Database
 * Handles conversion of enums and lists to/from database-compatible types
 */
class Converters {

    // Gender conversions
    @TypeConverter
    fun fromGender(gender: Gender?): String? = gender?.name

    @TypeConverter
    fun toGender(gender: String?): Gender? = gender?.let { Gender.valueOf(it) }

    // FitnessLevel conversions
    @TypeConverter
    fun fromFitnessLevel(level: FitnessLevel): String = level.name

    @TypeConverter
    fun toFitnessLevel(level: String): FitnessLevel = FitnessLevel.valueOf(level)

    // ExerciseType conversions
    @TypeConverter
    fun fromExerciseType(type: ExerciseType): String = type.name

    @TypeConverter
    fun toExerciseType(type: String): ExerciseType = ExerciseType.valueOf(type)

    // DifficultyLevel conversions
    @TypeConverter
    fun fromDifficultyLevel(level: DifficultyLevel): String = level.name

    @TypeConverter
    fun toDifficultyLevel(level: String): DifficultyLevel = DifficultyLevel.valueOf(level)

    // WorkoutType conversions
    @TypeConverter
    fun fromWorkoutType(type: WorkoutType): String = type.name

    @TypeConverter
    fun toWorkoutType(type: String): WorkoutType = WorkoutType.valueOf(type)

    // Mood conversions
    @TypeConverter
    fun fromMood(mood: Mood?): String? = mood?.name

    @TypeConverter
    fun toMood(mood: String?): Mood? = mood?.let { Mood.valueOf(it) }

    // MuscleGroup list conversions (stored as comma-separated string)
    @TypeConverter
    fun fromMuscleGroupList(groups: List<MuscleGroup>): String {
        return groups.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toMuscleGroupList(groups: String): List<MuscleGroup> {
        return if (groups.isEmpty()) emptyList()
        else groups.split(",").map { MuscleGroup.valueOf(it.trim()) }
    }

    // Equipment list conversions (stored as comma-separated string)
    @TypeConverter
    fun fromEquipmentList(equipment: List<Equipment>): String {
        return equipment.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toEquipmentList(equipment: String): List<Equipment> {
        return if (equipment.isEmpty()) emptyList()
        else equipment.split(",").map { Equipment.valueOf(it.trim()) }
    }

    // String list conversions (for exercise IDs, etc.)
    @TypeConverter
    fun fromStringList(strings: List<String>): String {
        return strings.joinToString(",")
    }

    @TypeConverter
    fun toStringList(strings: String): List<String> {
        return if (strings.isEmpty()) emptyList()
        else strings.split(",").map { it.trim() }
    }
}