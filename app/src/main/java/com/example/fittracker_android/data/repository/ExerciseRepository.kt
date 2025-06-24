package com.example.fittracker_android.data.repository

import com.example.fittracker_android.data.local.dao.ExerciseDao
import com.example.fittracker_android.data.local.entities.ExerciseEntity
import com.example.fittracker_android.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository for Exercise operations
 * Handles business logic for exercise management
 */
class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()



    fun getExercisesByMuscleGroup(muscleGroup: MuscleGroup): Flow<List<ExerciseEntity>> =
        exerciseDao.getExercisesByMuscleGroup(muscleGroup.name)

    fun getCustomExercisesByUser(userId: String): Flow<List<ExerciseEntity>> =
        exerciseDao.getCustomExercisesByUser(userId)

    fun searchExercises(query: String): Flow<List<ExerciseEntity>> =
        exerciseDao.searchExercises(query)

    suspend fun getExerciseById(exerciseId: String): ExerciseEntity? =
        exerciseDao.getExerciseById(exerciseId)

    suspend fun getAllExercisesList(): List<ExerciseEntity> =
        exerciseDao.getAllExercisesList()

    suspend fun getExercisesByTypeList(type: ExerciseType): List<ExerciseEntity> =
        exerciseDao.getExercisesByTypeList(type)

    suspend fun createExercise(
        name: String,
        description: String,
        instructions: String,
        type: ExerciseType,
        muscleGroups: List<MuscleGroup>,
        difficulty: DifficultyLevel = DifficultyLevel.INTERMEDIATE,
        equipmentRequired: List<Equipment> = emptyList(),
        userId: String? = null
    ): Result<ExerciseEntity> {
        return try {
            val exercise = ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                instructions = instructions,
                type = type,
                muscleGroups = muscleGroups.joinToString(",") { it.name },
                difficulty = difficulty,
                equipmentRequired = equipmentRequired.joinToString(",") { it.name },
                isCustom = userId != null,
                createdBy = userId
            )

            exerciseDao.insertExercise(exercise)
            Result.success(exercise)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExercise(exercise: ExerciseEntity): Result<Unit> {
        return try {
            exerciseDao.updateExercise(exercise)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExercise(exerciseId: String): Result<Unit> {
        return try {
            exerciseDao.deleteExerciseById(exerciseId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun populateDefaultExercises() {
        return try {
            val existingCount = exerciseDao.getExerciseCount()
            if (existingCount > 0) return

            val defaultExercises = getDefaultExercises()
            exerciseDao.insertExercises(defaultExercises)
        } catch (e: Exception) {
            android.util.Log.e("ExerciseRepository", "Error populating default exercises: ${e.message}")
            throw e
        }
    }

    private fun getDefaultExercises(): List<ExerciseEntity> {
        return listOf(
            ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = "Push-ups",
                description = "Classic bodyweight chest exercise",
                instructions = "Start in plank position, lower body to ground, push back up",
                type = ExerciseType.STRENGTH,
                muscleGroups = "CHEST,SHOULDERS,TRICEPS",
                difficulty = DifficultyLevel.BEGINNER,
                equipmentRequired = "",
                defaultSets = 3,
                defaultReps = 12
            ),
            ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = "Bench Press",
                description = "Classic barbell chest exercise",
                instructions = "Lie on bench, lower bar to chest, press up",
                type = ExerciseType.STRENGTH,
                muscleGroups = "CHEST,SHOULDERS,TRICEPS",
                difficulty = DifficultyLevel.INTERMEDIATE,
                equipmentRequired = "BARBELL,BENCH",
                defaultSets = 3,
                defaultReps = 10
            ),
            ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = "Pull-ups",
                description = "Bodyweight back exercise",
                instructions = "Hang from bar, pull body up until chin over bar",
                type = ExerciseType.STRENGTH,
                muscleGroups = "BACK,BICEPS",
                difficulty = DifficultyLevel.ADVANCED,
                equipmentRequired = "PULL_UP_BAR",
                defaultSets = 3,
                defaultReps = 8
            ),
            ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = "Running",
                description = "Cardiovascular exercise",
                instructions = "Maintain steady pace, breathe rhythmically",
                type = ExerciseType.CARDIO,
                muscleGroups = "CARDIO_RESPIRATORY,QUADRICEPS,CALVES",
                difficulty = DifficultyLevel.BEGINNER,
                equipmentRequired = "",
                defaultDuration = 30,
                defaultDistance = 5.0f
            ),
            ExerciseEntity(
                id = UUID.randomUUID().toString(),
                name = "Squats",
                description = "Fundamental leg exercise",
                instructions = "Stand with feet shoulder-width apart, lower hips back and down",
                type = ExerciseType.STRENGTH,
                muscleGroups = "QUADRICEPS,GLUTES,HAMSTRINGS",
                difficulty = DifficultyLevel.BEGINNER,
                equipmentRequired = "",
                defaultSets = 3,
                defaultReps = 15
            )
        )
    }
}