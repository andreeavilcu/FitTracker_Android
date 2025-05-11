package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fittracker_android.R


class LoginFragment : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterButton: Button

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

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()

            // Updated to match navigation graph action ID
            val action = LoginFragmentDirections.actionLoginToRegisterStep1(
                email = email
            )
            findNavController().navigate(action)
        }

        goToRegisterButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Updated to match navigation graph action ID
            val action = LoginFragmentDirections.actionLoginToRegisterStep1(
                email = if (email.isNotEmpty()) email else null,
                password = if (password.isNotEmpty()) password else null
            )
            findNavController().navigate(action)
        }
    }
}