package com.example.fittracker_android.data.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fittracker_android.data.api.model.ExerciseApiModel
import com.example.fittracker_android.data.api.model.NutritionApiModel
import com.example.fittracker_android.data.api.model.QuoteApiModel
import com.example.fittracker_android.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel pentru gestionarea datelor din API
 */

class ApiDataViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiUiState())
    val uiState: StateFlow<ApiUiState> = _uiState.asStateFlow()

    private val _externalExercises = MutableStateFlow<List<ExerciseApiModel>>(emptyList())
    val externalExercises: StateFlow<List<ExerciseApiModel>> = _externalExercises.asStateFlow()

    private val _nutritionData = MutableStateFlow<List<NutritionApiModel>>(emptyList())
    val nutritionData: StateFlow<List<NutritionApiModel>> = _nutritionData.asStateFlow()

    private val _motivationalQuotes = MutableStateFlow<List<QuoteApiModel>>(emptyList())
    val motivationalQuotes: StateFlow<List<QuoteApiModel>> = _motivationalQuotes.asStateFlow()

    fun loadMotivationalQuotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            android.util.Log.d("ApiDataViewModel", "🚀 Starting to load motivational quotes...")

            val result = apiRepository.getMotivationalQuotes()

            _uiState.value = if (result.isSuccess) {
                val quotes = result.getOrElse { emptyList() }
                android.util.Log.d("ApiDataViewModel", "✅ Successfully loaded ${quotes.size} quotes")
                _motivationalQuotes.value = quotes
                _uiState.value.copy(isLoading = false)
            } else {
                val error = result.exceptionOrNull()?.message ?: "Unknown error"
                android.util.Log.e("ApiDataViewModel", "❌ Failed to load quotes: $error")
                _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load quotes: $error"
                )
            }
        }
    }

    /**
     * Încarcă exerciții externe pe baza grupului muscular
     */
    fun loadExercisesByMuscle(muscle: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = apiRepository.getExercisesByMuscle(muscle.lowercase())

            _uiState.value = if (result.isSuccess) {
                _externalExercises.value = result.getOrElse { emptyList() }
                _uiState.value.copy(isLoading = false)
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load exercises: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    /**
     * Încarcă exerciții externe pe baza tipului
     */
    fun loadExercisesByType(type: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = apiRepository.getExercisesByType(type.lowercase())

            _uiState.value = if (result.isSuccess) {
                _externalExercises.value = result.getOrElse { emptyList() }
                _uiState.value.copy(isLoading = false)
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load exercises: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    /**
     * Încarcă informații nutriționale pentru o mâncare
     */
    fun loadNutritionInfo(foodName: String) {
        if (foodName.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = apiRepository.getNutritionInfo(foodName.trim())

            _uiState.value = if (result.isSuccess) {
                _nutritionData.value = result.getOrElse { emptyList() }
                _uiState.value.copy(isLoading = false)
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load nutrition info: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    /**
     * Curăță erorile
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Curăță datele
     */
    fun clearData() {
        _externalExercises.value = emptyList()
        _nutritionData.value = emptyList()
        _uiState.value = ApiUiState()
    }



}

/**
 * UI State pentru API data
 */
data class ApiUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)


/**
 * Factory pentru ApiDataViewModel
 */
class ApiDataViewModelFactory(
    private val apiRepository: ApiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApiDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApiDataViewModel(apiRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}