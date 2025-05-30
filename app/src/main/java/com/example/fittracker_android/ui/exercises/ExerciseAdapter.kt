package com.example.fittracker_android.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker_android.R
import com.example.fittracker_android.data.local.entities.ExerciseEntity
import com.example.fittracker_android.data.model.DifficultyLevel
import com.example.fittracker_android.data.model.ExerciseType
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

class ExerciseAdapter(
    private val onExerciseClick: (ExerciseEntity) -> Unit
) : ListAdapter<ExerciseEntity, ExerciseAdapter.ExerciseViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseAdapter.ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view, onExerciseClick)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExerciseViewHolder(
        itemView: View,
        private val onExerciseClick: (ExerciseEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView){

        private val chipExerciseType: Chip = itemView.findViewById(R.id.chipExerciseType)
        private val chipDifficulty: Chip = itemView.findViewById(R.id.chipDifficulty)
        private val textExerciseName: TextView = itemView.findViewById(R.id.textExerciseName)
        private val textExerciseDescription: TextView = itemView.findViewById(R.id.textExerciseDescription)
        private val textMuscleGroups: TextView = itemView.findViewById(R.id.textMuscleGroups)
        private val textExerciseDefaults: TextView = itemView.findViewById(R.id.textExerciseDefaults)
        private val textEquipment: TextView = itemView.findViewById(R.id.textEquipment)

        fun bind(exercise: ExerciseEntity) {
            textExerciseName.text = exercise.name
            textExerciseDescription.text = exercise.description

            chipExerciseType.text = exercise.type.name
            chipDifficulty.text = exercise.difficulty.name

            val muscleGroups = exercise.muscleGroups
                .split(",").joinToString(" • ") { it.trim().replace("_", " ") }
            textMuscleGroups.text = muscleGroups

            textExerciseDefaults.text = formatDefaults(exercise)

            if (exercise.equipmentRequired.isNotEmpty() && exercise.equipmentRequired != "NONE") {
                val equipment = exercise.equipmentRequired
                    .split(",").joinToString(", ") { it.trim().replace("_", " ") }
                textEquipment.text = "Equipment: $equipment"
                textEquipment.isVisible = true
            } else {
                textEquipment.isVisible = false
            }

            itemView.setOnClickListener {
                onExerciseClick(exercise)
            }
        }

        private fun formatDefaults(exercise: ExerciseEntity): String {
            return when {
                exercise.defaultSets != null && exercise.defaultReps != null -> {
                    "${exercise.defaultSets} x ${exercise.defaultReps}"
                }
                exercise.defaultDuration != null -> {
                    "${exercise.defaultDuration} min"
                }
                exercise.defaultDistance != null -> {
                    "${exercise.defaultDistance} km"
                }
                else -> "Custom"
            }
        }

    }

    private class ExerciseDiffCallback : DiffUtil.ItemCallback<ExerciseEntity>() {
        override fun areItemsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExerciseEntity, newItem: ExerciseEntity): Boolean {
            return oldItem == newItem
        }
    }

}