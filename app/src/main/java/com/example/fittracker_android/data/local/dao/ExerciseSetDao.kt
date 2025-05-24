package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.ExerciseSetEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for ExerciseSet operations - tracks individual sets of strength exercises
 */
@Dao
interface ExerciseSetDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSet(exerciseSet: ExerciseSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(exerciseSets: List<ExerciseSetEntity>)

    // READ
    @Query("SELECT * FROM exercise_sets WHERE id = :setId")
    suspend fun getExerciseSetById(setId: String): ExerciseSetEntity?

    @Query("SELECT * FROM exercise_sets WHERE exerciseLogId = :exerciseLogId ORDER BY setNumber ASC")
    fun getSetsByExerciseLog(exerciseLogId: String): Flow<List<ExerciseSetEntity>>

    @Query("SELECT * FROM exercise_sets WHERE exerciseLogId = :exerciseLogId ORDER BY setNumber ASC")
    suspend fun getSetsByExerciseLogList(exerciseLogId: String): List<ExerciseSetEntity>

    @Query("SELECT * FROM exercise_sets WHERE exerciseLogId = :exerciseLogId AND isCompleted = 1 ORDER BY setNumber ASC")
    suspend fun getCompletedSetsByExerciseLog(exerciseLogId: String): List<ExerciseSetEntity>

    // Get personal records
    @Query("""
        SELECT es.* FROM exercise_sets es
        INNER JOIN exercise_logs el ON es.exerciseLogId = el.id
        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id
        WHERE wl.userId = :userId AND el.exerciseId = :exerciseId 
        AND es.weight IS NOT NULL AND es.isCompleted = 1
        ORDER BY es.weight DESC, es.reps DESC
        LIMIT 1
    """)
    suspend fun getPersonalRecordByWeight(userId: String, exerciseId: String): ExerciseSetEntity?

    @Query("""
        SELECT es.* FROM exercise_sets es
        INNER JOIN exercise_logs el ON es.exerciseLogId = el.id
        INNER JOIN workout_logs wl ON el.workoutLogId = wl.id
        WHERE wl.userId = :userId AND el.exerciseId = :exerciseId 
        AND es.reps IS NOT NULL AND es.isCompleted = 1
        ORDER BY es.reps DESC, es.weight DESC
        LIMIT 1
    """)
    suspend fun getPersonalRecordByReps(userId: String, exerciseId: String): ExerciseSetEntity?

    // UPDATE
    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSetEntity)

    @Query("UPDATE exercise_sets SET isCompleted = :isCompleted WHERE id = :setId")
    suspend fun updateSetCompletion(setId: String, isCompleted: Boolean)

    @Query("UPDATE exercise_sets SET reps = :reps, weight = :weight WHERE id = :setId")
    suspend fun updateSetStats(setId: String, reps: Int?, weight: Float?)

    // DELETE
    @Delete
    suspend fun deleteExerciseSet(exerciseSet: ExerciseSetEntity)

    @Query("DELETE FROM exercise_sets WHERE id = :setId")
    suspend fun deleteExerciseSetById(setId: String)

    @Query("DELETE FROM exercise_sets WHERE exerciseLogId = :exerciseLogId")
    suspend fun deleteSetsByExerciseLog(exerciseLogId: String)

    // STATISTICS
    @Query("SELECT COUNT(*) FROM exercise_sets WHERE exerciseLogId = :exerciseLogId AND isCompleted = 1")
    suspend fun getCompletedSetCount(exerciseLogId: String): Int

    @Query("SELECT SUM(reps) FROM exercise_sets WHERE exerciseLogId = :exerciseLogId AND isCompleted = 1 AND reps IS NOT NULL")
    suspend fun getTotalRepsForExerciseLog(exerciseLogId: String): Int?

    @Query("SELECT AVG(weight) FROM exercise_sets WHERE exerciseLogId = :exerciseLogId AND isCompleted = 1 AND weight IS NOT NULL")
    suspend fun getAverageWeightForExerciseLog(exerciseLogId: String): Float?
}