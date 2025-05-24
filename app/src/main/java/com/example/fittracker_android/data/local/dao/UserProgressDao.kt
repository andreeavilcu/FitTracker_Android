package com.example.fittracker_android.data.local.dao

import androidx.room.*
import com.example.fittracker_android.data.local.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for UserProgress operations - tracks fitness progress over time
 */
@Dao
interface UserProgressDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressRecord(progress: UserProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressRecords(progressList: List<UserProgressEntity>)

    // READ
    @Query("SELECT * FROM user_progress WHERE id = :progressId")
    suspend fun getProgressRecordById(progressId: String): UserProgressEntity?

    @Query("SELECT * FROM user_progress WHERE userId = :userId ORDER BY recordDate DESC")
    fun getProgressByUser(userId: String): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE userId = :userId ORDER BY recordDate DESC")
    suspend fun getProgressByUserList(userId: String): List<UserProgressEntity>

    @Query("SELECT * FROM user_progress WHERE userId = :userId ORDER BY recordDate DESC LIMIT 1")
    suspend fun getLatestProgressRecord(userId: String): UserProgressEntity?

    @Query("SELECT * FROM user_progress WHERE userId = :userId ORDER BY recordDate ASC LIMIT 1")
    suspend fun getFirstProgressRecord(userId: String): UserProgressEntity?

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND recordDate >= :startDate AND recordDate <= :endDate ORDER BY recordDate ASC")
    fun getProgressInDateRange(userId: String, startDate: Long, endDate: Long): Flow<List<UserProgressEntity>>

    // Get specific metrics over time
    @Query("SELECT * FROM user_progress WHERE userId = :userId AND weight IS NOT NULL ORDER BY recordDate ASC")
    fun getWeightProgress(userId: String): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND bodyFatPercentage IS NOT NULL ORDER BY recordDate ASC")
    fun getBodyFatProgress(userId: String): Flow<List<UserProgressEntity>>

    // Get recent records for dashboard
    @Query("SELECT * FROM user_progress WHERE userId = :userId ORDER BY recordDate DESC LIMIT :limit")
    suspend fun getRecentProgressRecords(userId: String, limit: Int = 5): List<UserProgressEntity>

    // UPDATE
    @Update
    suspend fun updateProgressRecord(progress: UserProgressEntity)

    // DELETE
    @Delete
    suspend fun deleteProgressRecord(progress: UserProgressEntity)

    @Query("DELETE FROM user_progress WHERE id = :progressId")
    suspend fun deleteProgressRecordById(progressId: String)

    @Query("DELETE FROM user_progress WHERE userId = :userId")
    suspend fun deleteAllProgressByUser(userId: String)
}