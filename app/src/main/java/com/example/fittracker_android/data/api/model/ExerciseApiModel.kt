package com.example.fittracker_android.data.api.model

import com.google.gson.annotations.SerializedName

/**
 * Model pentru ExerciseDB API response
 */
data class ExerciseApiResponse(
    @SerializedName("results")
    val results: List<ExerciseApiModel>
)

data class ExerciseApiModel(
    @SerializedName("name")
    val name: String,

    @SerializedName("force")
    val force: String?,

    @SerializedName("level")
    val level: String,

    @SerializedName("mechanic")
    val mechanic: String?,

    @SerializedName("equipment")
    val equipment: String?,

    // FIXED: Făcut nullable pentru a evita crash-ul
    @SerializedName("primaryMuscles")
    val primaryMuscles: List<String>?,

    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>?,

    @SerializedName("instructions")
    val instructions: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("images")
    val images: List<String>?
) {
    fun getInstructionsList(): List<String> {
        return instructions.split(". ").filter { it.isNotBlank() }
    }

    fun getAllMuscles(): List<String> {
        val primary = primaryMuscles ?: emptyList()
        val secondary = secondaryMuscles ?: emptyList()
        return primary + secondary
    }
}