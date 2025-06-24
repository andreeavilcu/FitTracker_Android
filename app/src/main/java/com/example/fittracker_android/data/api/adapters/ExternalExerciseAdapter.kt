package com.example.fittracker_android.data.api.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker_android.R
import com.example.fittracker_android.data.api.model.ExerciseApiModel
import com.google.android.material.chip.Chip

/**
 * Adapter pentru exerciții din API external
 */

class ExternalExerciseAdapter(
    private val onExerciseClick: (ExerciseApiModel) -> Unit
) : ListAdapter<ExerciseApiModel, ExternalExerciseAdapter.ViewHolder>(ExerciseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_external_exercise, parent, false)
        return ViewHolder(view, onExerciseClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onExerciseClick: (ExerciseApiModel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val textExerciseName: TextView = itemView.findViewById(R.id.textExerciseName)
        private val textDifficulty: TextView = itemView.findViewById(R.id.textDifficulty)
        private val textEquipment: TextView = itemView.findViewById(R.id.textEquipment)
        private val textMuscles: TextView = itemView.findViewById(R.id.textMuscles)
        private val textInstructions: TextView = itemView.findViewById(R.id.textInstructions)
        private val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)

        fun bind(exercise: ExerciseApiModel) {
            textExerciseName.text = exercise.name ?: "Unknown Exercise"
            textDifficulty.text = "Level: ${exercise.level ?: "Unknown"}"
            textEquipment.text = "Equipment: ${exercise.equipment ?: "None"}"

            val muscles = exercise.getAllMuscles()
                .joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } }
            textMuscles.text = if (muscles.isNotEmpty()) "Muscles: $muscles" else "Muscles: Not specified"

            val instructions = exercise.instructions?.let { instr ->
                if (instr.length > 100) {
                    instr.take(100) + "..."
                } else {
                    instr
                }
            } ?: "No instructions available"
            textInstructions.text = instructions

            chipCategory.text = (exercise.category ?: "General").replaceFirstChar { it.uppercase() }

            itemView.setOnClickListener {
                onExerciseClick(exercise)
            }
        }
    }

    private class ExerciseDiffCallback : DiffUtil.ItemCallback<ExerciseApiModel>() {
        override fun areItemsTheSame(oldItem: ExerciseApiModel, newItem: ExerciseApiModel): Boolean {
            return (oldItem.name ?: "") == (newItem.name ?: "")
        }

        override fun areContentsTheSame(oldItem: ExerciseApiModel, newItem: ExerciseApiModel): Boolean {
            return oldItem == newItem
        }
    }
}