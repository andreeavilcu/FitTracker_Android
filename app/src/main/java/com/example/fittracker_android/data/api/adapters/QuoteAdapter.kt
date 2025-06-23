package com.example.fittracker_android.data.api.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fittracker_android.R
import com.example.fittracker_android.data.api.model.QuoteApiModel

/**
 * Adapter pentru citate motivaționale din API
 */
class QuoteAdapter : ListAdapter<QuoteApiModel, QuoteAdapter.ViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textQuote: TextView = itemView.findViewById(R.id.textQuote)
        private val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)

        fun bind(quote: QuoteApiModel) {
            // 💭 Quote text cu formatare specială
            textQuote.text = "\"${quote.text}\""

            // ✍️ Author cu formatare
            textAuthor.text = if (quote.author.isNotEmpty()) {
                "— ${quote.author}"
            } else {
                "— Unknown"
            }
        }
    }

    private class QuoteDiffCallback : DiffUtil.ItemCallback<QuoteApiModel>() {
        override fun areItemsTheSame(oldItem: QuoteApiModel, newItem: QuoteApiModel): Boolean {
            return oldItem.text == newItem.text && oldItem.author == newItem.author
        }

        override fun areContentsTheSame(oldItem: QuoteApiModel, newItem: QuoteApiModel): Boolean {
            return oldItem == newItem
        }
    }
}