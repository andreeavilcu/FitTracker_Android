package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterButton: Button
    private lateinit var progressBar: ProgressBar

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (requireActivity().application as FitTrackerApplication).userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        goToRegisterButton = view.findViewById(R.id.goToRegisterButton)
        progressBar = view.findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    emailEditText.error = "Email is required"
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    passwordEditText.error = "Password is required"
                    return@setOnClickListener
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailEditText.error = "Invalid email format"
                    return@setOnClickListener
                }
            }

            viewModel.login(email, password)
        }

        goToRegisterButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val action = LoginFragmentDirections.actionLoginToRegisterStep1(
                email = if (email.isNotEmpty()) email else null,
                password = if (password.isNotEmpty()) password else null
            )
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                progressBar.isVisible = state.isLoading
                loginButton.isEnabled = !state.isLoading
                goToRegisterButton.isEnabled = !state.isLoading

                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }

                if (state.isLoggedIn) {
                    val userEmail = viewModel.currentUser.first()?.email ?: ""
                    val action = LoginFragmentDirections.actionLoginToProfile(email = userEmail)
                    findNavController().navigate(action)
                }
            }
        }
    }
}