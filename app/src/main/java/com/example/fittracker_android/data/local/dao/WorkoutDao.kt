package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.WorkoutEntity
import com.example.fittracker_android.data.model.WorkoutType
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Workout operations
 */
@Dao
interface WorkoutDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    // READ
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String): WorkoutEntity?

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWorkoutsByUser(userId: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getWorkoutsByUserList(userId: String): List<WorkoutEntity>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND type = :type ORDER BY timestamp DESC")
    fun getWorkoutsByUserAndType(userId: String, type: WorkoutType): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isCompleted = 1 ORDER BY timestamp DESC")
    fun getCompletedWorkoutsByUser(userId: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isTemplate = 1 ORDER BY name ASC")
    fun getWorkoutTemplatesByUser(userId: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getWorkoutsByUserInDateRange(userId: String, startTime: Long, endTime: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE userId = :userId AND isCompleted = 1 ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentCompletedWorkouts(userId: String, limit: Int = 5): List<WorkoutEntity>

    // UPDATE
    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("UPDATE workouts SET isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :workoutId")
    suspend fun updateWorkoutCompletion(workoutId: String, isCompleted: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE workouts SET duration = :duration, caloriesBurned = :calories, updatedAt = :updatedAt WHERE id = :workoutId")
    suspend fun updateWorkoutStats(workoutId: String, duration: Int, calories: Int?, updatedAt: Long = System.currentTimeMillis())

    // DELETE
    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: String)

    @Query("DELETE FROM workouts WHERE userId = :userId")
    suspend fun deleteAllWorkoutsByUser(userId: String)

    // STATISTICS
    @Query("SELECT COUNT(*) FROM workouts WHERE userId = :userId AND isCompleted = 1")
    suspend fun getCompletedWorkoutCount(userId: String): Int

    @Query("SELECT SUM(duration) FROM workouts WHERE userId = :userId AND isCompleted = 1")
    suspend fun getTotalWorkoutDuration(userId: String): Int?

    @Query("SELECT AVG(duration) FROM workouts WHERE userId = :userId AND isCompleted = 1")
    suspend fun getAverageWorkoutDuration(userId: String): Float?

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE userId = :userId AND isCompleted = 1 AND caloriesBurned IS NOT NULL")
    suspend fun getTotalCaloriesBurned(userId: String): Int?

    @Query("SELECT COUNT(*) FROM workouts WHERE userId = :userId AND isCompleted = 1 AND timestamp >= :startTime AND timestamp <= :endTime")
    suspend fun getWorkoutCountInDateRange(userId: String, startTime: Long, endTime: Long): Int
}