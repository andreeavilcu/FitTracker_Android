package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.ExerciseEntity
import com.example.fittracker_android.data.model.ExerciseType
import com.example.fittracker_android.data.model.MuscleGroup
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Exercise operations
 */
@Dao
interface ExerciseDao {
    //Create
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    //Read
    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: String): ExerciseEntity?

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query ("SELECT * FROM exercises WHERE type = :type ORDER BY name ASC")
    fun getExerciseByType(type: ExerciseType): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE muscleGroups LIKE '%' || :muscleGroup || '%' ORDER BY name ASC")
    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE isCustom = 1 AND createdBy = :userId ORDER BY createdAt DESC")
    fun getCustomExercisesByUser(userId: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchExercises(searchQuery: String): Flow<List<ExerciseEntity>>

    // Get exercises for RecyclerView - returns list immediately
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    suspend fun getAllExercisesList(): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE type = :type ORDER BY name ASC")
    suspend fun getExercisesByTypeList(type: ExerciseType): List<ExerciseEntity>

    //UPDATE
    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    //DELETE
    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("DELETE FROM exercises WHERE id = :exerciseId")
    suspend fun deleteExerciseById(exerciseId: String)

    @Query("DELETE FROM exercises WHERE isCustom = 1 AND createdBy = :userId")
    suspend fun deleteAllCustomExercisesByUser(userId: String)

    // UTILITY
    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE type = :type")
    suspend fun getExerciseCountByType(type: ExerciseType): Int
}
