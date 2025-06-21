package com.example.fittracker_android.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import com.example.fittracker_android.data.local.Units
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    // UI Components
    private lateinit var toolbar: MaterialToolbar
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var switchNotifications: SwitchMaterial
    private lateinit var switchKeepScreenOn: SwitchMaterial
    private lateinit var buttonUnits: Button
    private lateinit var buttonRestTime: Button
    private lateinit var buttonWeeklyGoal: Button
    private lateinit var buttonResetSettings: Button

    // SharedPreferences Manager
    private val prefsManager by lazy {
        (requireActivity().application as FitTrackerApplication).userPreferencesManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        loadCurrentSettings()
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        switchNotifications = view.findViewById(R.id.switchNotifications)
        switchKeepScreenOn = view.findViewById(R.id.switchKeepScreenOn)
        buttonUnits = view.findViewById(R.id.buttonUnits)
        buttonRestTime = view.findViewById(R.id.buttonRestTime)
        buttonWeeklyGoal = view.findViewById(R.id.buttonWeeklyGoal)
        buttonResetSettings = view.findViewById(R.id.buttonResetSettings)
    }

    private fun loadCurrentSettings() {
        try {
            // Load current values from SharedPreferences
            switchDarkMode.isChecked = prefsManager.getDarkMode()
            switchNotifications.isChecked = prefsManager.getNotificationsEnabled()
            switchKeepScreenOn.isChecked = prefsManager.getKeepScreenOn()

            // Update button texts
            updateUnitsButton()
            updateRestTimeButton()
            updateWeeklyGoalButton()

        } catch (e: Exception) {
            Toast.makeText(context, "Error loading settings: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        // Toolbar back button
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // DARK MODE SWITCH
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.setDarkMode(isChecked)
            showToast("🌙 Dark Mode: ${if (isChecked) "ON" else "OFF"}")
        }

        // NOTIFICATIONS SWITCH
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.setNotificationsEnabled(isChecked)
            showToast("🔔 Notifications: ${if (isChecked) "ON" else "OFF"}")
        }

        // KEEP SCREEN ON SWITCH
        switchKeepScreenOn.setOnCheckedChangeListener { _, isChecked ->
            prefsManager.setKeepScreenOn(isChecked)
            showToast("📱 Keep Screen On: ${if (isChecked) "ON" else "OFF"}")
        }

        // UNITS BUTTON
        buttonUnits.setOnClickListener {
            showUnitsDialog()
        }

        // REST TIME BUTTON
        buttonRestTime.setOnClickListener {
            showRestTimeDialog()
        }

        // WEEKLY GOAL BUTTON
        buttonWeeklyGoal.setOnClickListener {
            showWeeklyGoalDialog()
        }

        // RESET SETTINGS BUTTON
        buttonResetSettings.setOnClickListener {
            showResetConfirmDialog()
        }
    }

    private fun showUnitsDialog() {
        val currentUnits = prefsManager.getUnits()
        val options = arrayOf("Metric (kg, cm)", "Imperial (lbs, ft/in)")
        val currentSelection = if (currentUnits == Units.METRIC) 0 else 1

        AlertDialog.Builder(requireContext())
            .setTitle("📏 Choose Units")
            .setSingleChoiceItems(options, currentSelection) { dialog, which ->
                val newUnits = if (which == 0) Units.METRIC else Units.IMPERIAL
                prefsManager.setUnits(newUnits)
                updateUnitsButton()
                showToast("📏 Units changed to: ${newUnits.displayName}")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRestTimeDialog() {
        val currentRestTime = prefsManager.getDefaultRestTime()
        val options = arrayOf("30 seconds", "60 seconds", "90 seconds", "120 seconds", "180 seconds")
        val values = arrayOf(30, 60, 90, 120, 180)
        val currentSelection = values.indexOf(currentRestTime).takeIf { it >= 0 } ?: 1

        AlertDialog.Builder(requireContext())
            .setTitle("⏱️ Default Rest Time")
            .setSingleChoiceItems(options, currentSelection) { dialog, which ->
                val newRestTime = values[which]
                prefsManager.setDefaultRestTime(newRestTime)
                updateRestTimeButton()
                showToast("⏱️ Rest time set to: ${newRestTime}s")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showWeeklyGoalDialog() {
        val currentGoal = prefsManager.getWeeklyWorkoutGoal()
        val options = arrayOf("1 workout", "2 workouts", "3 workouts", "4 workouts", "5 workouts", "6 workouts", "7 workouts")
        val values = arrayOf(1, 2, 3, 4, 5, 6, 7)
        val currentSelection = values.indexOf(currentGoal).takeIf { it >= 0 } ?: 2

        AlertDialog.Builder(requireContext())
            .setTitle("🎯 Weekly Workout Goal")
            .setSingleChoiceItems(options, currentSelection) { dialog, which ->
                val newGoal = values[which]
                prefsManager.setWeeklyWorkoutGoal(newGoal)
                updateWeeklyGoalButton()
                showToast("🎯 Weekly goal set to: $newGoal workouts")
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showResetConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("🗑️ Reset All Settings")
            .setMessage("Are you sure you want to reset all settings to default values? This action cannot be undone.")
            .setPositiveButton("Reset") { _, _ ->
                resetAllSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun resetAllSettings() {
        try {
            prefsManager.clearAllPreferences()
            loadCurrentSettings() // Reload UI with default values
            showToast("🗑️ All settings reset to defaults")
        } catch (e: Exception) {
            showToast("❌ Error resetting settings: ${e.message}")
        }
    }

    private fun updateUnitsButton() {
        buttonUnits.text = prefsManager.getUnits().displayName
    }

    private fun updateRestTimeButton() {
        buttonRestTime.text = "${prefsManager.getDefaultRestTime()}s"
    }

    private fun updateWeeklyGoalButton() {
        val goal = prefsManager.getWeeklyWorkoutGoal()
        buttonWeeklyGoal.text = "$goal workout${if (goal != 1) "s" else ""}"
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}