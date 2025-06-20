package com.example.fittracker_android.data.api

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
import com.example.fittracker_android.data.api.adapters.ExternalExerciseAdapter
import com.example.fittracker_android.data.api.adapters.NutritionAdapter
import com.example.fittracker_android.data.repository.ApiRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class ApiDataFragment : Fragment() {

    private lateinit var recyclerViewExercises: RecyclerView
    private lateinit var recyclerViewNutrition: RecyclerView
    private lateinit var searchEditText: TextInputEditText
    private lateinit var nutritionSearchEditText: TextInputEditText
    private lateinit var chipGroupMuscles: ChipGroup
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var buttonSearchNutrition: MaterialButton

    private lateinit var externalExerciseAdapter: ExternalExerciseAdapter
    private lateinit var nutritionAdapter: NutritionAdapter

    private val viewModel: ApiDataViewModel by viewModels {
        ApiDataViewModelFactory(ApiRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerViews()
        setupSearch()
        observeViewModel()

        // Încarcă câteva exerciții implicit
        viewModel.loadExercisesByMuscle("chest")
    }

    private fun initializeViews(view: View) {
        recyclerViewExercises = view.findViewById(R.id.recyclerViewExercises)
        recyclerViewNutrition = view.findViewById(R.id.recyclerViewNutrition)
        searchEditText = view.findViewById(R.id.searchEditText)
        nutritionSearchEditText = view.findViewById(R.id.nutritionSearchEditText)
        chipGroupMuscles = view.findViewById(R.id.chipGroupMuscles)
        progressIndicator = view.findViewById(R.id.progressIndicator)
        buttonSearchNutrition = view.findViewById(R.id.buttonSearchNutrition)
    }

    private fun setupRecyclerViews() {
        // Setup External Exercises RecyclerView
        externalExerciseAdapter = ExternalExerciseAdapter { exercise ->
            Toast.makeText(context, "Selected: ${exercise.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerViewExercises.apply {
            adapter = externalExerciseAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Setup Nutrition RecyclerView
        nutritionAdapter = NutritionAdapter()

        recyclerViewNutrition.apply {
            adapter = nutritionAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setupSearch() {
        // Muscle group chips
        chipGroupMuscles.setOnCheckedStateChangeListener { _, checkedIds ->
            when {
                checkedIds.contains(R.id.chipChest) -> viewModel.loadExercisesByMuscle("chest")
                checkedIds.contains(R.id.chipBack) -> viewModel.loadExercisesByMuscle("lats")
                checkedIds.contains(R.id.chipLegs) -> viewModel.loadExercisesByMuscle("quadriceps")
                checkedIds.contains(R.id.chipArms) -> viewModel.loadExercisesByMuscle("biceps")
            }
        }

        buttonSearchNutrition.setOnClickListener {
            val foodName = nutritionSearchEditText.text.toString().trim()
            if (foodName.isNotEmpty()) {
                viewModel.loadNutritionInfo(foodName)
            }
        }

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                progressIndicator.isVisible = state.isLoading

                state.error?.let { error ->
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.externalExercises.collect { exercises ->
                externalExerciseAdapter.submitList(exercises)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nutritionData.collect { nutrition ->
                nutritionAdapter.submitList(nutrition)
            }
        }
    }


}