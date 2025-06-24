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
    private val defaultQuotes = listOf(
        QuoteApiModel("The only bad workout is the one that didn't happen.", "Unknown"),
        QuoteApiModel("Your body can do it. It's your mind you need to convince.", "Unknown"),
        QuoteApiModel("Strength doesn't come from what you can do. It comes from overcoming the things you once thought you couldn't.", "Rikki Rogers"),
        QuoteApiModel("The groundwork for all happiness is good health.", "Leigh Hunt"),
        QuoteApiModel("Take care of your body. It's the only place you have to live.", "Jim Rohn"),
        QuoteApiModel("Success isn't always about greatness. It's about consistency.", "Dwayne Johnson"),
        QuoteApiModel("You don't have to be extreme, just consistent.", "Unknown"),
        QuoteApiModel("Push yourself because no one else is going to do it for you.", "Unknown"),
        QuoteApiModel("Great things never come from comfort zones.", "Unknown"),
        QuoteApiModel("Don't limit your challenges, challenge your limits.", "Unknown"),
        QuoteApiModel("The pain you feel today will be the strength you feel tomorrow.", "Unknown"),
        QuoteApiModel("Champions keep playing until they get it right.", "Billie Jean King"),
        QuoteApiModel("Exercise is a celebration of what your body can do. Not a punishment for what you ate.", "Unknown"),
        QuoteApiModel("A healthy outside starts from the inside.", "Robert Urich"),
        QuoteApiModel("You are stronger than your excuses.", "Unknown")
    )


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

    suspend fun getMotivationalQuotes(): Result<List<QuoteApiModel>> {
        return withContext(Dispatchers.IO) {
            try {
                android.util.Log.d("ApiRepository", "🚀 Trying to load quotes from API...")

                val response = ApiManager.quoteApiService.getMotivationalQuotes()

                if (response.isSuccessful) {
                    val apiQuotes = response.body()

                    if (!apiQuotes.isNullOrEmpty() && apiQuotes.all { !it.text.isNullOrBlank() }) {
                        android.util.Log.d("ApiRepository", "✅ Successfully loaded ${apiQuotes.size} quotes from API")
                        return@withContext Result.success(apiQuotes)
                    }
                }

                android.util.Log.w("ApiRepository", "⚠️ API failed or returned invalid data, using default quotes")
                getDefaultQuotes()

            } catch (e: Exception) {
                android.util.Log.w("ApiRepository", "⚠️ Network error, using default quotes: ${e.message}")
                getDefaultQuotes()
            }
        }
    }

    /**
     * Obține citate implicite aleatorii
     */
    private fun getDefaultQuotes(): Result<List<QuoteApiModel>> {
        return try {
            val randomQuotes = defaultQuotes.shuffled().take(5)
            android.util.Log.d("ApiRepository", "📚 Using ${randomQuotes.size} default quotes")
            Result.success(randomQuotes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Metodă pentru a forța încărcarea citatelor implicite (pentru testare)
     */
    suspend fun loadDefaultQuotesOnly(): Result<List<QuoteApiModel>> {
        return withContext(Dispatchers.IO) {
            android.util.Log.d("ApiRepository", "📚 Loading default quotes only...")
            getDefaultQuotes()
        }
    }

}
