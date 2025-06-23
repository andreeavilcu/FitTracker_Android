package com.example.fittracker_android.data.api.service

import com.example.fittracker_android.data.api.model.QuoteApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuoteApiService {

    // ✅ VERSIUNEA CORECTĂ - fără API key manual și fără categorie
    @GET("quotes")
    suspend fun getMotivationalQuotes(): Response<List<QuoteApiModel>>

    // 🎯 VERSIUNEA CU CATEGORIE OPȚIONALĂ (dacă vrem să testăm categorii)
    @GET("quotes")
    suspend fun getQuotesByCategory(
        @Query("category") category: String
    ): Response<List<QuoteApiModel>>
}