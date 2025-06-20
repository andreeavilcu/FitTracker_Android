package com.example.fittracker_android.data.api.service

import com.example.fittracker_android.data.api.model.ExerciseApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Service pentru exerciții externe
 */
interface ExerciseApiService {

    @GET("exercises")
    suspend fun getExercisesByMuscle(
        @Query("muscle") muscle: String,
        @Query("offset") offset: Int = 0
    ): Response<List<ExerciseApiModel>>

    @GET("exercises")
    suspend fun getExercisesByType(
        @Query("type") type: String,
        @Query("offset") offset: Int = 0
    ): Response<List<ExerciseApiModel>>

    @GET("exercises")
    suspend fun getExercisesByDifficulty(
        @Query("difficulty") difficulty: String,
        @Query("offset") offset: Int = 0
    ): Response<List<ExerciseApiModel>>
}