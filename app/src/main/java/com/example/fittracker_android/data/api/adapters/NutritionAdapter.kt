package com.example.fittracker_android.data.api.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker_android.R
import com.example.fittracker_android.data.api.model.NutritionApiModel

/**
 * Adapter pentru informații nutriționale din API
 */

class NutritionAdapter: ListAdapter<NutritionApiModel, NutritionAdapter.ViewHolder>(NutritionDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nutrition, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val textFoodName: TextView = itemView.findViewById(R.id.textFoodName)
        private val textCalories: TextView = itemView.findViewById(R.id.textCalories)
        private val textProtein: TextView = itemView.findViewById(R.id.textProtein)
        private val textCarbs: TextView = itemView.findViewById(R.id.textCarbs)
        private val textFat: TextView = itemView.findViewById(R.id.textFat)
        private val textServing: TextView = itemView.findViewById(R.id.textServing)

        fun bind(nutrition: NutritionApiModel) {
            textFoodName.text = nutrition.name.replaceFirstChar { it.uppercase() }
            textCalories.text = "Calories: ${nutrition.calories.toInt()}"
            textProtein.text = "Protein: ${String.format("%.1f", nutrition.proteinG)}g"
            textCarbs.text = "Carbs: ${String.format("%.1f", nutrition.carbohydratesTotalG)}g"
            textFat.text = "Fat: ${String.format("%.1f", nutrition.fatTotalG)}g"
            textServing.text = "Serving: ${nutrition.servingSizeG.toInt()}g"
        }
    }

    private class NutritionDiffCallback : DiffUtil.ItemCallback<NutritionApiModel>() {
        override fun areItemsTheSame(oldItem: NutritionApiModel, newItem: NutritionApiModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: NutritionApiModel, newItem: NutritionApiModel): Boolean {
            return oldItem == newItem
        }
    }

}