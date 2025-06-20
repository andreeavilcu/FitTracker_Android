package com.example.fittracker_android.ui.exercises

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
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import com.example.fittracker_android.data.model.DifficultyLevel
import com.example.fittracker_android.data.model.ExerciseType
import com.example.fittracker_android.data.model.MuscleGroup
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class AddExerciseFragment : Fragment() {

    // Views
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var instructionsEditText: EditText
    private lateinit var chipGroupType: ChipGroup
    private lateinit var chipGroupMuscles: ChipGroup
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar

    // ViewModel
    private val exercisesViewModel: ExercisesViewModel by viewModels {
        ExercisesViewModelFactory(
            (requireActivity().application as FitTrackerApplication).exerciseRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupClickListeners()
        observeViewModel()
    }

    private fun initializeViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        nameEditText = view.findViewById(R.id.nameEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        instructionsEditText = view.findViewById(R.id.instructionsEditText)
        chipGroupType = view.findViewById(R.id.chipGroupType)
        chipGroupMuscles = view.findViewById(R.id.chipGroupMuscles)
        saveButton = view.findViewById(R.id.saveButton)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupClickListeners() {
        // Toolbar back button
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Save button
        saveButton.setOnClickListener {
            saveExercise()
        }
    }

    private fun saveExercise() {
        // Get values from form
        val name = nameEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val instructions = instructionsEditText.text.toString().trim()

        // Validation
        if (!validateInput(name, instructions)) {
            return
        }

        // Get selected exercise type
        val selectedType = getSelectedExerciseType()
        if (selectedType == null) {
            Toast.makeText(context, "Please select an exercise type", Toast.LENGTH_SHORT).show()
            return
        }

        // Get selected muscle groups
        val selectedMuscles = getSelectedMuscleGroups()
        if (selectedMuscles.isEmpty()) {
            Toast.makeText(context, "Please select at least one muscle group", Toast.LENGTH_SHORT).show()
            return
        }

        // Get current user ID (you might need to get this from AuthViewModel)
        val userId = getCurrentUserId()

        // Save exercise
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                progressBar.isVisible = true
                saveButton.isEnabled = false

                val exerciseRepository = (requireActivity().application as FitTrackerApplication).exerciseRepository

                val result = exerciseRepository.createExercise(
                    name = name,
                    description = description.ifEmpty { "Custom exercise created by user" },
                    instructions = instructions,
                    type = selectedType,
                    muscleGroups = selectedMuscles,
                    difficulty = DifficultyLevel.INTERMEDIATE, // Default difficulty
                    equipmentRequired = emptyList(), // For now, no equipment
                    userId = userId
                )

                progressBar.isVisible = false
                saveButton.isEnabled = true

                if (result.isSuccess) {
                    Toast.makeText(context, "Exercise saved successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to save exercise: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                progressBar.isVisible = false
                saveButton.isEnabled = true
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateInput(name: String, instructions: String): Boolean {
        when {
            name.isEmpty() -> {
                nameEditText.error = "Exercise name is required"
                nameEditText.requestFocus()
                return false
            }
            name.length < 3 -> {
                nameEditText.error = "Exercise name must be at least 3 characters"
                nameEditText.requestFocus()
                return false
            }
            instructions.isEmpty() -> {
                instructionsEditText.error = "Instructions are required"
                instructionsEditText.requestFocus()
                return false
            }
            instructions.length < 10 -> {
                instructionsEditText.error = "Please provide detailed instructions"
                instructionsEditText.requestFocus()
                return false
            }
        }
        return true
    }

    private fun getSelectedExerciseType(): ExerciseType? {
        val checkedChipId = chipGroupType.checkedChipId
        return when (checkedChipId) {
            R.id.chipStrength -> ExerciseType.STRENGTH
            R.id.chipCardio -> ExerciseType.CARDIO
            R.id.chipFlexibility -> ExerciseType.FLEXIBILITY
            R.id.chipBalance -> ExerciseType.BALANCE
            else -> null
        }
    }

    private fun getSelectedMuscleGroups(): List<MuscleGroup> {
        val selectedMuscles = mutableListOf<MuscleGroup>()

        for (i in 0 until chipGroupMuscles.childCount) {
            val chip = chipGroupMuscles.getChildAt(i) as? Chip
            if (chip?.isChecked == true) {
                when (chip.id) {
                    R.id.chipChest -> selectedMuscles.add(MuscleGroup.CHEST)
                    R.id.chipBack -> selectedMuscles.add(MuscleGroup.BACK)
                    R.id.chipShoulders -> selectedMuscles.add(MuscleGroup.SHOULDERS)
                    R.id.chipBiceps -> selectedMuscles.add(MuscleGroup.BICEPS)
                    R.id.chipTriceps -> selectedMuscles.add(MuscleGroup.TRICEPS)
                    R.id.chipQuadriceps -> selectedMuscles.add(MuscleGroup.QUADRICEPS)
                    R.id.chipHamstrings -> selectedMuscles.add(MuscleGroup.HAMSTRINGS)
                    R.id.chipGlutes -> selectedMuscles.add(MuscleGroup.GLUTES)
                }
            }
        }

        return selectedMuscles
    }

    private fun getCurrentUserId(): String {
        // TODO: Get this from AuthViewModel or UserRepository
        // For now, return a default ID - you should get the actual logged-in user
        return "current_user_id"
    }

    private fun observeViewModel() {
        // If you need to observe any ViewModel state changes
        viewLifecycleOwner.lifecycleScope.launch {
            exercisesViewModel.uiState.collect { state ->
                // Handle any UI state changes if needed
                state.error?.let { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}