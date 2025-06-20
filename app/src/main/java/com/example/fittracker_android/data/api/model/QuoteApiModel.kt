package com.example.fittracker_android.data.api.model
import com.google.gson.annotations.SerializedName
/**
 * Model pentru motivational quotes (bonus API)
 */
data class QuoteApiModel(
    @SerializedName("text")
    val text: String,

    @SerializedName("author")
    val author: String
)