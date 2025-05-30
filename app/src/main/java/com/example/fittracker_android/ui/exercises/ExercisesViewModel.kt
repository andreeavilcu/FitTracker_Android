package com.example.fittracker_android.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fittracker_android.data.local.entities.ExerciseEntity
import com.example.fittracker_android.data.model.ExerciseType
import com.example.fittracker_android.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedExerciseType = MutableStateFlow<ExerciseType?>(null)
    val selectedExerciseType: StateFlow<ExerciseType?> = _selectedExerciseType.asStateFlow()

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    private val allExercises = exerciseRepository.getAllExercises()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredExercises: StateFlow<List<ExerciseEntity>> = combine(
        allExercises,
        searchQuery,
        selectedExerciseType
    ) { exercises, query, type ->
        filterExercises(exercises, query, type)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            filteredExercises.collect { exercises ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEmpty = exercises.isEmpty()
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateExerciseTypeFilter(type: ExerciseType?) {
        _selectedExerciseType.value = type
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedExerciseType.value = null
    }

    fun onExerciseSelected(exercise: ExerciseEntity) {
        _uiState.value = _uiState.value.copy(
            selectedExercise = exercise
        )
    }

    fun clearSelectedExercise() {
        _uiState.value = _uiState.value.copy(
            selectedExercise = null
        )
    }

    private fun filterExercises(
        exercises: List<ExerciseEntity>,
        query: String,
        type: ExerciseType?
    ): List<ExerciseEntity> {
        return exercises.filter { exercise ->
            val matchesType = type == null || exercise.type == type

            val matchesQuery = query.isBlank() ||
                    exercise.name.contains(query, ignoreCase = true) ||
                    exercise.description.contains(query, ignoreCase = true) ||
                    exercise.muscleGroups.contains(query, ignoreCase = true)

            matchesType && matchesQuery
        }.sortedBy { it.name }
    }

    fun getExerciseStatsByType(): Map<ExerciseType, Int> {
        return allExercises.value.groupBy { it.type }
            .mapValues { it.value.size }
    }

    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
    }
}

data class ExercisesUiState(
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val selectedExercise: ExerciseEntity? = null,
    val error: String? = null
)

class ExercisesViewModelFactory(
    private val exerciseRepository: ExerciseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExercisesViewModel(exerciseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}