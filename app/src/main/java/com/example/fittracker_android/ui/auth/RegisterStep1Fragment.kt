package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fittracker_android.R

class RegisterStep1Fragment : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nextButton: Button
    private val args: RegisterStep1FragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        nextButton = view.findViewById(R.id.nextButton)

        // Pre-fill fields if coming from login
        args.email?.let { emailEditText.setText(it) }
        args.password?.let { passwordEditText.setText(it) }

        nextButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            // Validation
            when {
                email.isEmpty() -> {
                    emailEditText.error = "Email is required"
                    return@setOnClickListener
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailEditText.error = "Invalid email format"
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    passwordEditText.error = "Password is required"
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    passwordEditText.error = "Password must be at least 6 characters"
                    return@setOnClickListener
                }
            }

            // Navigate to step 2 with email
            val action = RegisterStep1FragmentDirections.actionRegisterStep1ToRegisterStep2(email)

            // Pass password through bundle (since it's not in the navigation args)
            findNavController().navigate(action.actionId, bundleOf(
                "email" to email,
                "password" to password
            ))
        }
    }
}