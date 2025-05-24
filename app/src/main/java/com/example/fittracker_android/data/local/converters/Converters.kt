package com.example.fittracker_android.data.local.converters

import androidx.room.TypeConverter
import com.example.fittracker_android.data.model.FitnessLevel
import com.example.fittracker_android.data.model.Gender

/**
 * Type Converters for Room
 *
 * Room doesn't know how to store enums directly in the database,
 * so we need to tell it how to convert them to/from strings
 */
class Converters {

    // Convert Gender enum to String for storage
    @TypeConverter
    fun fromGender(gender: Gender?): String? {
        return gender?.name
    }

    // Convert String back to Gender enum when reading
    @TypeConverter
    fun toGender(gender: String?): Gender? {
        return gender?.let { Gender.valueOf(it) }
    }

    // Convert FitnessLevel enum to String
    @TypeConverter
    fun fromFitnessLevel(level: FitnessLevel): String {
        return level.name
    }

    // Convert String back to FitnessLevel enum
    @TypeConverter
    fun toFitnessLevel(level: String): FitnessLevel {
        return FitnessLevel.valueOf(level)
    }
}