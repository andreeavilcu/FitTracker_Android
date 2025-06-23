package com.example.fittracker_android.data.api.service

import com.example.fittracker_android.data.api.model.QuoteApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuoteApiService {

    // QuoteApiService.kt - versiunea corectă
    @GET("quotes")
    suspend fun getMotivationalQuotes(): Response<List<QuoteApiModel>>
}