package com.example.fittracker_android.data.api

import android.content.Context
import com.example.fittracker_android.data.api.service.ExerciseApiService
import com.example.fittracker_android.data.api.service.NutritionApiService
import com.example.fittracker_android.data.api.service.QuoteApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton pentru gestionarea API-urilor
 */

object ApiManager {
    private const val BASE_URL_NINJA = "https://api.api-ninjas.com/v1/"
    const val API_KEY = "k1IZKlS/j+fLHV5jyGYOlg==Ed70GotaaF2qOUl7"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("X-Api-Key", API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofitNinja = Retrofit.Builder()
        .baseUrl(BASE_URL_NINJA)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val exerciseApiService: ExerciseApiService = retrofitNinja.create(ExerciseApiService::class.java)
    val nutritionApiService: NutritionApiService = retrofitNinja.create(NutritionApiService::class.java)
    val quoteApiService: QuoteApiService = retrofitNinja.create(QuoteApiService::class.java)
}