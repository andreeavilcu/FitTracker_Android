package com.example.fittracker_android.data.repository

import com.example.fittracker_android.data.local.dao.UserProgressDao
import com.example.fittracker_android.data.local.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repository for UserProgress operations
 * Handles fitness progress tracking business logic
 */
class UserProgressRepository(private val userProgressDao: UserProgressDao) {

    // Observe user progress
    fun getProgressByUser(userId: String): Flow<List<UserProgressEntity>> =
        userProgressDao.getProgressByUser(userId)

    // Get progress as list for RecyclerView
    suspend fun getProgressByUserList(userId: String): List<UserProgressEntity> =
        userProgressDao.getProgressByUserList(userId)

    // Get latest progress record
    suspend fun getLatestProgressRecord(userId: String): UserProgressEntity? =
        userProgressDao.getLatestProgressRecord(userId)

    // Get first progress record (to show improvement)
    suspend fun getFirstProgressRecord(userId: String): UserProgressEntity? =
        userProgressDao.getFirstProgressRecord(userId)

    // Get specific metric progress
    fun getWeightProgress(userId: String): Flow<List<UserProgressEntity>> =
        userProgressDao.getWeightProgress(userId)

    fun getBodyFatProgress(userId: String): Flow<List<UserProgressEntity>> =
        userProgressDao.getBodyFatProgress(userId)

    // Add new progress record
    suspend fun addProgressRecord(
        userId: String,
        weight: Float? = null,
        bodyFatPercentage: Float? = null,
        chest: Float? = null,
        waist: Float? = null,
        hips: Float? = null,
        biceps: Float? = null,
        thighs: Float? = null,
        restingHeartRate: Int? = null,
        vo2Max: Float? = null,
        pushUpCount: Int? = null,
        pullUpCount: Int? = null,
        plankDuration: Int? = null,
        runDistance: Float? = null,
        runTime: Int? = null,
        notes: String? = null,
        progressPhotoUrl: String? = null
    ): Result<UserProgressEntity> {
        return try {
            val progressRecord = UserProgressEntity(
                id = UUID.randomUUID().toString(),
                userId = userId,
                weight = weight,
                bodyFatPercentage = bodyFatPercentage,
                chest = chest,
                waist = waist,
                hips = hips,
                biceps = biceps,
                thighs = thighs,
                restingHeartRate = restingHeartRate,
                vo2Max = vo2Max,
                pushUpCount = pushUpCount,
                pullUpCount = pullUpCount,
                plankDuration = plankDuration,
                runDistance = runDistance,
                runTime = runTime,
                notes = notes,
                progressPhotoUrl = progressPhotoUrl
            )

            userProgressDao.insertProgressRecord(progressRecord)
            Result.success(progressRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update progress record
    suspend fun updateProgressRecord(progressRecord: UserProgressEntity): Result<Unit> {
        return try {
            userProgressDao.updateProgressRecord(progressRecord)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete progress record
    suspend fun deleteProgressRecord(progressId: String): Result<Unit> {
        return try {
            userProgressDao.deleteProgressRecordById(progressId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get progress statistics
    suspend fun getProgressStats(userId: String): ProgressStats {
        return try {
            val allProgress = userProgressDao.getProgressByUserList(userId)
            val latestProgress = userProgressDao.getLatestProgressRecord(userId)
            val firstProgress = userProgressDao.getFirstProgressRecord(userId)

            ProgressStats(
                totalRecords = allProgress.size,
                latestWeight = latestProgress?.weight,
                weightChange = calculateWeightChange(firstProgress?.weight, latestProgress?.weight),
                latestBodyFat = latestProgress?.bodyFatPercentage,
                bodyFatChange = calculateBodyFatChange(firstProgress?.bodyFatPercentage, latestProgress?.bodyFatPercentage),
                daysSinceStart = calculateDaysSinceStart(firstProgress?.recordDate),
                improvementTrend = calculateImprovementTrend(allProgress)
            )
        } catch (e: Exception) {
            ProgressStats()
        }
    }

    // Helper functions for statistics
    private fun calculateWeightChange(first: Float?, latest: Float?): Float? {
        return if (first != null && latest != null) latest - first else null
    }

    private fun calculateBodyFatChange(first: Float?, latest: Float?): Float? {
        return if (first != null && latest != null) latest - first else null
    }

    private fun calculateDaysSinceStart(firstRecordDate: Long?): Int? {
        return if (firstRecordDate != null) {
            val daysDiff = (System.currentTimeMillis() - firstRecordDate) / (1000 * 60 * 60 * 24)
            daysDiff.toInt()
        } else null
    }

    private fun calculateImprovementTrend(progressList: List<UserProgressEntity>): String {
        if (progressList.size < 2) return "Not enough data"

        val sortedProgress = progressList.sortedBy { it.recordDate }
        val first = sortedProgress.first()
        val latest = sortedProgress.last()

        // Simple trend analysis based on weight (you can expand this)
        return when {
            first.weight != null && latest.weight != null -> {
                val change = latest.weight!! - first.weight!!
                when {
                    change < -2 -> "Weight Loss Trend"
                    change > 2 -> "Weight Gain Trend"
                    else -> "Stable Weight"
                }
            }
            else -> "Tracking Progress"
        }
    }
}

// Data class for progress statistics
data class ProgressStats(
    val totalRecords: Int = 0,
    val latestWeight: Float? = null,
    val weightChange: Float? = null,
    val latestBodyFat: Float? = null,
    val bodyFatChange: Float? = null,
    val daysSinceStart: Int? = null,
    val improvementTrend: String = "No data"
)