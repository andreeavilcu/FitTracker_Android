package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import kotlinx.coroutines.launch

class RegisterStep2Fragment : Fragment() {
    private lateinit var emailInfoTextView: TextView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    private val args: RegisterStep2FragmentArgs by navArgs()

    // Get the shared ViewModel
    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (requireActivity().application as FitTrackerApplication).userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_step2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        emailInfoTextView = view.findViewById(R.id.emailInfoTextView)
        firstNameEditText = view.findViewById(R.id.firstNameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        registerButton = view.findViewById(R.id.registerButton)
        progressBar = view.findViewById(R.id.progressBar)
        // Show the email from previous screen
        emailInfoTextView.text = "Selected email: ${args.email}"

        // Handle register button click
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val ageText = ageEditText.text.toString().trim()

            // Validation
            when {
                firstName.isEmpty() -> {
                    firstNameEditText.error = "First name is required"
                    return@setOnClickListener
                }
                lastName.isEmpty() -> {
                    lastNameEditText.error = "Last name is required"
                    return@setOnClickListener
                }
            }

            // Parse age (optional field)
            val age = if (ageText.isNotEmpty()) {
                ageText.toIntOrNull()?.takeIf { it in 1..150 }
            } else null

            if (ageText.isNotEmpty() && age == null) {
                ageEditText.error = "Please enter a valid age"
                return@setOnClickListener
            }

            // Get password from navigation args (passed from RegisterStep1)
            val password = args.password
            if (password.isEmpty()) {
                Toast.makeText(context, "Password missing, please go back", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Perform registration
            viewModel.register(
                email = args.email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                age = age
            )
        }

        // Observe registration state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Enable/disable button based on loading state
                progressBar.isVisible = state.isLoading
                registerButton.isEnabled = !state.isLoading

                // Show error if any
                state.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }

                // Navigate to profile if registration successful
                if (state.isLoggedIn) {
                    val action = RegisterStep2FragmentDirections.actionRegisterStep2ToProfile(args.email)
                    findNavController().navigate(action)
                }
            }
        }
    }
}