package com.example.fittracker_android.data.api.service

import com.example.fittracker_android.data.api.model.NutritionApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * API Service pentru informații nutriționale
 */

interface NutritionApiService {

    @GET("nutrition")
    suspend fun getNutritionInfo(
        @Query("query") foodName: String,
        @Header("X-Api-Key") apiKey: String
    ): Response<List<NutritionApiModel>>
}