package com.example.fittracker_android.ui.auth

import android.app.AlertDialog
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
import com.example.fittracker_android.data.local.Units
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var exercisesButton: MaterialButton
    private lateinit var apiDataButton: MaterialButton
    private lateinit var workoutsButton: MaterialButton
    private lateinit var progressButton: MaterialButton
    private lateinit var settingsButton: MaterialButton // ADDED: Settings button

    // SharedPreferences Manager
    private val prefsManager by lazy {
        (requireActivity().application as FitTrackerApplication).userPreferencesManager
    }

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
        apiDataButton = view.findViewById(R.id.apiDataButton)
        workoutsButton = view.findViewById(R.id.workoutsButton)
        progressButton = view.findViewById(R.id.progressButton)
        settingsButton = view.findViewById(R.id.settingsButton) // ADDED: Initialize settings button
    }

    private fun setupClickListeners() {
        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        // Navigare către exerciții locale
        exercisesButton.setOnClickListener {
            findNavController().navigate(R.id.exercisesFragment)
        }

        // Navigare către date externe API
        apiDataButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_api_data)
        }

        // ADDED: Navigare către Settings
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        // TEST SHARED PREFERENCES - DOUBLE CLICK pe My Workouts
        var clickCount = 0
        workoutsButton.setOnClickListener {
            clickCount++
            if (clickCount == 1) {
                Toast.makeText(context, "🔄 Click again to TEST SharedPreferences!", Toast.LENGTH_SHORT).show()
                // Reset counter după 2 secunde
                view?.postDelayed({ clickCount = 0 }, 2000)
            } else if (clickCount == 2) {
                testSharedPreferences()
                clickCount = 0
            }
        }

        // SHOW CURRENT SETTINGS - Long press pe Track Progress
        progressButton.setOnLongClickListener {
            showCurrentPreferencesDialog()
            true
        }

        // Short click pe progress - placeholder
        progressButton.setOnClickListener {
            Toast.makeText(context, "📊 Progress tracking coming soon! Long press to see settings.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun testSharedPreferences() {
        try {
            // STEP 1: Salvează setări noi pentru test
            prefsManager.setDarkMode(true)
            prefsManager.setWeeklyWorkoutGoal(5)
            prefsManager.setDefaultRestTime(90)
            prefsManager.setNotificationsEnabled(false)
            prefsManager.setUnits(Units.IMPERIAL)
            prefsManager.setKeepScreenOn(true)

            Toast.makeText(context, "✅ Test settings SAVED! Long press 'Track Progress' to view or go to Settings.", Toast.LENGTH_LONG).show()

            // Log pentru debugging
            android.util.Log.d("SharedPrefsTest", "✅ All SharedPreferences saved successfully!")

        } catch (e: Exception) {
            Toast.makeText(context, "❌ Error saving test settings: ${e.message}", Toast.LENGTH_LONG).show()
            android.util.Log.e("SharedPrefsTest", "Error testing SharedPreferences", e)
        }
    }

    private fun showCurrentPreferencesDialog() {
        try {
            val settings = StringBuilder()
            settings.append("📱 CURRENT SETTINGS:\n\n")
            settings.append("🌙 Dark Mode: ${if (prefsManager.getDarkMode()) "ON" else "OFF"}\n\n")
            settings.append("📏 Units: ${prefsManager.getUnits().displayName}\n\n")
            settings.append("🔔 Notifications: ${if (prefsManager.getNotificationsEnabled()) "ON" else "OFF"}\n\n")
            settings.append("⏱️ Rest Time: ${prefsManager.getDefaultRestTime()} seconds\n\n")
            settings.append("🎯 Weekly Goal: ${prefsManager.getWeeklyWorkoutGoal()} workouts\n\n")
            settings.append("📱 Keep Screen On: ${if (prefsManager.getKeepScreenOn()) "ON" else "OFF"}\n\n")
            settings.append("💡 Tip: Go to Settings to change these values!")

            AlertDialog.Builder(requireContext())
                .setTitle("🔧 Current App Settings")
                .setMessage(settings.toString())
                .setPositiveButton("OK") { _, _ -> }
                .setNeutralButton("Clear All") { _, _ ->
                    clearAllPreferences()
                }
                .setNegativeButton("Settings") { _, _ ->
                    findNavController().navigate(R.id.action_profile_to_settings)
                }
                .show()

        } catch (e: Exception) {
            Toast.makeText(context, "❌ Error reading settings: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearAllPreferences() {
        try {
            prefsManager.clearAllPreferences()
            Toast.makeText(context, "🗑️ All settings reset to defaults! Check Settings or test again.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "❌ Error clearing settings: ${e.message}", Toast.LENGTH_SHORT).show()
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