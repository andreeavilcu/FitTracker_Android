package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for WorkoutLog operations - tracks completed workout sessions
 */

@Dao
interface WorkoutLogDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(workoutLog: WorkoutLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLogs(workoutLogs: List<WorkoutLogEntity>)

    //READ
    @Query("SELECT * FROM workout_logs WHERE id = :logId")
    suspend fun getWorkoutLogById(logId: String): WorkoutLogEntity?

    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY startTime DESC")
    fun getWorkoutLogsByUser(userId: String): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY startTime DESC")
    suspend fun getWorkoutLogsByUserList(userId: String): List<WorkoutLogEntity>

    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND startTime >= :startTime AND startTime <= :endTime ORDER BY startTime DESC")
    fun getWorkoutLogsInDateRange(userId: String, startTime: Long, endTime: Long): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs WHERE workoutId = :workoutId ORDER BY startTime DESC")
    fun getLogsByWorkoutId(workoutId: String): Flow<List<WorkoutLogEntity>>

    // Get recent logs for dashboard
    @Query("SELECT * FROM workout_logs WHERE userId = :userId ORDER BY startTime DESC LIMIT :limit")
    suspend fun getRecentWorkoutLogs(userId: String, limit: Int = 10): List<WorkoutLogEntity>

    // Get today's workouts
    @Query("SELECT * FROM workout_logs WHERE userId = :userId AND startTime >= :todayStart AND startTime <= :todayEnd ORDER BY startTime DESC")
    suspend fun getTodayWorkoutLogs(userId: String, todayStart: Long, todayEnd: Long): List<WorkoutLogEntity>

    // UPDATE
    @Update
    suspend fun updateWorkoutLog(workoutLog: WorkoutLogEntity)

    // DELETE
    @Delete
    suspend fun deleteWorkoutLog(workoutLog: WorkoutLogEntity)

    @Query("DELETE FROM workout_logs WHERE id = :logId")
    suspend fun deleteWorkoutLogById(logId: String)

    @Query("DELETE FROM workout_logs WHERE userId = :userId")
    suspend fun deleteAllWorkoutLogsByUser(userId: String)

    // STATISTICS
    @Query("SELECT COUNT(*) FROM workout_logs WHERE userId = :userId")
    suspend fun getWorkoutLogCount(userId: String): Int

    @Query("SELECT SUM(duration) FROM workout_logs WHERE userId = :userId")
    suspend fun getTotalWorkoutTime(userId: String): Int?

    @Query("SELECT AVG(duration) FROM workout_logs WHERE userId = :userId")
    suspend fun getAverageWorkoutTime(userId: String): Float?

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs WHERE userId = :userId AND caloriesBurned IS NOT NULL")
    suspend fun getTotalCaloriesBurnedFromLogs(userId: String): Int?

    @Query("SELECT AVG(perceivedEffort) FROM workout_logs WHERE userId = :userId AND perceivedEffort IS NOT NULL")
    suspend fun getAveragePerceivedEffort(userId: String): Float?

    // Get workout frequency (workouts per week)
    @Query("SELECT COUNT(*) FROM workout_logs WHERE userId = :userId AND startTime >= :weekStart")
    suspend fun getWorkoutsThisWeek(userId: String, weekStart: Long): Int

    @Query("SELECT COUNT(*) FROM workout_logs WHERE userId = :userId AND startTime >= :monthStart")
    suspend fun getWorkoutsThisMonth(userId: String, monthStart: Long): Int
}