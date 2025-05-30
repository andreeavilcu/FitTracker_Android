package com.example.fittracker_android.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker_android.FitTrackerApplication
import com.example.fittracker_android.R
import com.example.fittracker_android.data.model.ExerciseType
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch



class ExercisesFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: TextInputEditText
    private lateinit var filterButton: MaterialButton
    private lateinit var chipGroupExerciseTypes: ChipGroup
    private lateinit var emptyStateLayout: View
    private lateinit var fabAddExercise: FloatingActionButton

    private lateinit var exerciseAdapter: ExerciseAdapter

    private val viewModel: ExercisesViewModel by viewModels {
        ExercisesViewModelFactory(
            (requireActivity().application as FitTrackerApplication).exerciseRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerView()
        setupSearchAndFilters()
        observeViewModel()
        setupClickListeners()
    }

    private fun initializeViews(view: View){
        recyclerView = view.findViewById(R.id.recyclerViewExercises)
        searchEditText = view.findViewById(R.id.searchEditText)
        filterButton = view.findViewById(R.id.filterButton)
        chipGroupExerciseTypes = view.findViewById(R.id.chipGroupExerciseTypes)
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)
        fabAddExercise = view.findViewById(R.id.fabAddExercise)
    }
    
    private fun setupRecyclerView(){
        exerciseAdapter = ExerciseAdapter { exercise ->
            viewModel.onExerciseSelected(exercise)
            Toast.makeText(context, "Selected: ${exercise.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.apply {
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchAndFilters() {
        searchEditText.addTextChangedListener { text ->
            viewModel.updateSearchQuery(text?.toString() ?: "")
        }

        setupFilterChips()
    }

    private fun setupFilterChips(){
        chipGroupExerciseTypes.setOnCheckedStateChangeListener { group, checkedIds ->
            when {
                checkedIds.contains(R.id.chipAll) || checkedIds.isEmpty() -> {
                    viewModel.updateExerciseTypeFilter(null)
                }

                checkedIds.contains(R.id.chipStrength) -> {
                    viewModel.updateExerciseTypeFilter(ExerciseType.STRENGTH)
                }
                checkedIds.contains(R.id.chipCardio) -> {
                    viewModel.updateExerciseTypeFilter(ExerciseType.CARDIO)
                }
                checkedIds.contains(R.id.chipFlexibility) -> {
                    viewModel.updateExerciseTypeFilter(ExerciseType.FLEXIBILITY)
                }
                checkedIds.contains(R.id.chipBalance) -> {
                    viewModel.updateExerciseTypeFilter(ExerciseType.BALANCE)
                }
            }
        }

    }
    private fun setupClickListeners() {
        filterButton.setOnClickListener {
            Toast.makeText(context, "Advanced filters coming soon!", Toast.LENGTH_SHORT).show()
        }

        fabAddExercise.setOnClickListener {
            Toast.makeText(context, "Add custom exercise coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredExercises.collect { exercises ->
                exerciseAdapter.submitList(exercises)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUIState(state)
            }
        }
    }

    private fun updateUIState(state: ExercisesUiState) {
        emptyStateLayout.isVisible = state.isEmpty && !state.isLoading
        recyclerView.isVisible = !state.isEmpty && !state.isLoading

        state.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun newInstance(): ExercisesFragment {
            return ExercisesFragment()
        }
    }



}