package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var exercisesButton: MaterialButton

    // Get the shared ViewModel
    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            (requireActivity().application as FitTrackerApplication).userRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initializeViews(view)
        setupClickListeners()
        observeCurrentUser()


    }

    private fun initializeViews(view: View) {
        emailTextView = view.findViewById(R.id.emailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)
        exercisesButton = view.findViewById(R.id.exercisesButton)
    }

    private fun setupClickListeners() {
        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        // NAVIGARE CĂTRE EXERCIȚII
        exercisesButton.setOnClickListener {
            // Creează un Bundle pentru a naviga către ExercisesFragment
            findNavController().navigate(R.id.exercisesFragment)
        }
    }

    private fun observeCurrentUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collect { user ->
                if (user != null) {
                    emailTextView.text = user.email
                } else {
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }
    }
}