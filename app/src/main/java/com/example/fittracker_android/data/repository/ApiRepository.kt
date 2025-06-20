package com.example.fittracker_android.data.repository

import com.example.fittracker_android.data.api.ApiManager
import com.example.fittracker_android.data.api.model.ExerciseApiModel
import com.example.fittracker_android.data.api.model.NutritionApiModel
import com.example.fittracker_android.data.api.model.QuoteApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository pentru toate API call-urile
 */
class ApiRepository {
    /**
     * Preia exerciții externe pe baza grupului muscular
     */

    suspend fun getExercisesByMuscle(muscle: String): Result<List<ExerciseApiModel>>{
        return withContext (Dispatchers.IO){
            try{
                val response = ApiManager.exerciseApiService.getExercisesByMuscle(muscle)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
            }
                else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))  }
            }
            catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Preia exerciții pe baza tipului
     */
    suspend fun getExercisesByType(type: String) : Result<List<ExerciseApiModel>>{
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiManager.exerciseApiService.getExercisesByType(type)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("API Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Preia informații nutriționale pentru o mâncare
     */
    suspend fun getNutritionInfo(foodName: String): Result<List<NutritionApiModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiManager.nutritionApiService.getNutritionInfo(
                    foodName, ApiManager.API_KEY
                )
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Nutrition API Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Preia citate motivaționale
     */
    suspend fun getMotivationalQuotes(): Result<List<QuoteApiModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiManager.quoteApiService.getMotivationalQuotes(ApiManager.API_KEY)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Quotes API Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


}