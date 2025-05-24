package com.example.fittracker_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button

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

        emailTextView = view.findViewById(R.id.emailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)

        // Observe the current user
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUser.collect { user ->
                if (user != null) {
                    // Show user data
                    emailTextView.text = user.email
                } else {
                    // No user logged in, go back to login
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        // Handle logout
        logoutButton.setOnClickListener {
            viewModel.logout()
            // Navigation will happen automatically when currentUser becomes null
        }
    }
}