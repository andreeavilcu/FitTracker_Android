package com.example.fittracker_android.data.repository

import com.example.fittracker_android.data.local.dao.*
import com.example.fittracker_android.data.local.entities.*
import com.example.fittracker_android.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository for Workout and WorkoutLog operations
 * Handles complex workout tracking business logic
 */
class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutLogDao: WorkoutLogDao,
    private val exerciseLogDao: ExerciseLogDao,
    private val exerciseSetDao: ExerciseSetDao
) {

    fun getWorkoutsByUser(userId: String): Flow<List<WorkoutEntity>> =
        workoutDao.getWorkoutsByUser(userId)

    suspend fun getWorkoutsByUserList(userId: String): List<WorkoutEntity> =
        workoutDao.getWorkoutsByUserList(userId)

    fun getWorkoutTemplatesByUser(userId: String): Flow<List<WorkoutEntity>> =
        workoutDao.getWorkoutTemplatesByUser(userId)

    suspend fun getWorkoutById(workoutId: String): WorkoutEntity? =
        workoutDao.getWorkoutById(workoutId)

    suspend fun createWorkout(
        userId: String,
        name: String,
        type: WorkoutType,
        exerciseIds: List<String> = emptyList(),
        isTemplate: Boolean = false
    ): Result<WorkoutEntity> {
        return try {
            val workout = WorkoutEntity(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = name,
                type = type,
                duration = 0,
                exercises = exerciseIds.joinToString(","),
                isTemplate = isTemplate
            )

            workoutDao.insertWorkout(workout)
            Result.success(workout)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getWorkoutLogsByUser(userId: String): Flow<List<WorkoutLogEntity>> =
        workoutLogDao.getWorkoutLogsByUser(userId)

    suspend fun getWorkoutLogsByUserList(userId: String): List<WorkoutLogEntity> =
        workoutLogDao.getWorkoutLogsByUserList(userId)

    suspend fun getRecentWorkoutLogs(userId: String, limit: Int = 10): List<WorkoutLogEntity> =
        workoutLogDao.getRecentWorkoutLogs(userId, limit)

    suspend fun startWorkoutSession(
        userId: String,
        workoutId: String? = null
    ): Result<WorkoutLogEntity> {
        return try {
            val workoutLog = WorkoutLogEntity(
                id = UUID.randomUUID().toString(),
                userId = userId,
                workoutId = workoutId,
                startTime = System.currentTimeMillis(),
                endTime = System.currentTimeMillis(),
                duration = 0
            )

            workoutLogDao.insertWorkoutLog(workoutLog)
            Result.success(workoutLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun finishWorkoutSession(
        workoutLogId: String,
        caloriesBurned: Int? = null,
        perceivedEffort: Int? = null,
        mood: Mood? = null,
        notes: String? = null
    ): Result<WorkoutLogEntity> {
        return try {
            val workoutLog = workoutLogDao.getWorkoutLogById(workoutLogId)
                ?: return Result.failure(Exception("Workout log not found"))

            val endTime = System.currentTimeMillis()
            val duration = ((endTime - workoutLog.startTime) / 1000 / 60).toInt()

            val updatedLog = workoutLog.copy(
                endTime = endTime,
                duration = duration,
                caloriesBurned = caloriesBurned,
                perceivedEffort = perceivedEffort,
                mood = mood,
                notes = notes
            )

            workoutLogDao.updateWorkoutLog(updatedLog)
            Result.success(updatedLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addExerciseToWorkout(
        workoutLogId: String,
        exerciseId: String,
        orderInWorkout: Int = 0
    ): Result<ExerciseLogEntity> {
        return try {
            val exerciseLog = ExerciseLogEntity(
                id = UUID.randomUUID().toString(),
                workoutLogId = workoutLogId,
                exerciseId = exerciseId,
                orderInWorkout = orderInWorkout
            )

            exerciseLogDao.insertExerciseLog(exerciseLog)
            Result.success(exerciseLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExerciseLogsByWorkout(workoutLogId: String): List<ExerciseLogEntity> =
        exerciseLogDao.getExerciseLogsByWorkoutLogList(workoutLogId)

    suspend fun addSetToExercise(
        exerciseLogId: String,
        setNumber: Int,
        reps: Int? = null,
        weight: Float? = null,
        duration: Int? = null,
        isWarmupSet: Boolean = false
    ): Result<ExerciseSetEntity> {
        return try {
            val exerciseSet = ExerciseSetEntity(
                id = UUID.randomUUID().toString(),
                exerciseLogId = exerciseLogId,
                reps = reps,
                weight = weight,
                duration = duration,
                setNumber = setNumber,
                isWarmupSet = isWarmupSet
            )

            exerciseSetDao.insertExerciseSet(exerciseSet)
            Result.success(exerciseSet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSetsByExerciseLog(exerciseLogId: String): List<ExerciseSetEntity> =
        exerciseSetDao.getSetsByExerciseLogList(exerciseLogId)

    suspend fun updateSetCompletion(setId: String, isCompleted: Boolean): Result<Unit> {
        return try {
            exerciseSetDao.updateSetCompletion(setId, isCompleted)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkoutStats(userId: String): WorkoutStats {
        return try {
            WorkoutStats(
                totalWorkouts = workoutLogDao.getWorkoutLogCount(userId),
                totalTime = workoutLogDao.getTotalWorkoutTime(userId) ?: 0,
                totalCalories = workoutLogDao.getTotalCaloriesBurnedFromLogs(userId) ?: 0,
                averageWorkoutTime = workoutLogDao.getAverageWorkoutTime(userId) ?: 0f,
                workoutsThisWeek = workoutLogDao.getWorkoutsThisWeek(userId, getWeekStartTime()),
                workoutsThisMonth = workoutLogDao.getWorkoutsThisMonth(userId, getMonthStartTime())
            )
        } catch (e: Exception) {
            WorkoutStats()
        }
    }

    private fun getWeekStartTime(): Long {
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = now
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getMonthStartTime(): Long {
        val now = System.currentTimeMillis()
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = now
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

data class WorkoutStats(
    val totalWorkouts: Int = 0,
    val totalTime: Int = 0,
    val totalCalories: Int = 0,
    val averageWorkoutTime: Float = 0f,
    val workoutsThisWeek: Int = 0,
    val workoutsThisMonth: Int = 0
)