package com.example.fittracker_android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fittracker_android.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Authentication
 *
 * ViewModels:
 * - Survive configuration changes (screen rotation)
 * - Hold UI-related data
 * - Connect UI to Repository/Database
 * - Handle business logic for the UI
 *
 * This ViewModel manages login, registration, and user state
 */
class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // UI State - what the screen should show
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    // Current logged-in user
    val currentUser = userRepository.observeLoggedInUser()

    // Login function
    fun login(email: String, password: String) {
        // Don't process if already loading
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.loginUser(email, password)

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // Register function
    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int?
    ) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.registerUser(
                email, password, firstName, lastName, age
            )

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    // Logout function
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _uiState.value = AuthUiState() // Reset to initial state
        }
    }

    // Clear any errors
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

// Data class to hold UI state
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

// Factory to create ViewModels with parameters
class AuthViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}