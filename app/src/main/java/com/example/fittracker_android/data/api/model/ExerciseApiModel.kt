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

    @SerializedName("primaryMuscles")
    val primaryMuscles: List<String>,

    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>,

    // SCHIMBAT: instructions ca String în loc de List<String>
    @SerializedName("instructions")
    val instructions: String,  // 👈 CORECTAT: String în loc de List<String>

    @SerializedName("category")
    val category: String,

    @SerializedName("images")
    val images: List<String>
) {
    // Helper function pentru a obține instrucțiunile ca listă
    fun getInstructionsList(): List<String> {
        return instructions.split(". ").filter { it.isNotBlank() }
    }
}