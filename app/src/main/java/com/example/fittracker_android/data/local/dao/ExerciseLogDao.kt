package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.ExerciseLogEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for ExerciseLog operations - tracks individual exercises within workouts
 */
@Dao
interface ExerciseLogDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLog(exerciseLog: ExerciseLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLogs(exerciseLogs: List<ExerciseLogEntity>)

    // READ
    @Query("SELECT * FROM exercise_logs WHERE id = :logId")
    suspend fun getExerciseLogById(logId: String): ExerciseLogEntity?

    @Query("SELECT * FROM exercise_logs WHERE workoutLogId = :workoutLogId ORDER BY orderInWorkout ASC")
    fun getExerciseLogsByWorkoutLog(workoutLogId: String): Flow<List<ExerciseLogEntity>>

    @Query("SELECT * FROM exercise_logs WHERE workoutLogId = :workoutLogId ORDER BY orderInWorkout ASC")
    suspend fun getExerciseLogsByWorkoutLogList(workoutLogId: String): List<ExerciseLogEntity>

    @Query("SELECT * FROM exercise_logs WHERE exerciseId = :exerciseId ORDER BY createdAt DESC")
    fun getLogsByExerciseId(exerciseId: String): Flow<List<ExerciseLogEntity>>

    // Get exercise logs with workout info for history
    @Query("""
        SELECT el.* FROM exercise_logs el
        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id
        WHERE wl.userId = :userId AND el.exerciseId = :exerciseId
        ORDER BY wl.startTime DESC
    """)
    fun getExerciseHistoryByUser(userId: String, exerciseId: String): Flow<List<ExerciseLogEntity>>

    // UPDATE
    @Update
    suspend fun updateExerciseLog(exerciseLog: ExerciseLogEntity)

    // DELETE
    @Delete
    suspend fun deleteExerciseLog(exerciseLog: ExerciseLogEntity)

    @Query("DELETE FROM exercise_logs WHERE id = :logId")
    suspend fun deleteExerciseLogById(logId: String)

    @Query("DELETE FROM exercise_logs WHERE workoutLogId = :workoutLogId")
    suspend fun deleteExerciseLogsByWorkoutLog(workoutLogId: String)

    // STATISTICS
    @Query("SELECT COUNT(*) FROM exercise_logs WHERE exerciseId = :exerciseId")
    suspend fun getExerciseLogCount(exerciseId: String): Int

    @Query("""
        SELECT COUNT(*) FROM exercise_logs el
        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id
        WHERE wl.userId = :userId AND el.exerciseId = :exerciseId
    """)
    suspend fun getExerciseLogCountByUser(userId: String, exerciseId: String): Int
}