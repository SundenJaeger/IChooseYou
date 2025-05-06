package com.android.ichooseyou.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.ichooseyou.activity.LotteryNumberAdapter.NumberViewHolder
import com.android.ichooseyou2.R
import com.google.android.material.card.MaterialCardView

class LotteryNumberAdapter(private var numbers: List<Int>) :
    RecyclerView.Adapter<NumberViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun updateNumbers(newNumbers: List<Int>) {
        numbers = newNumbers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lottery_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.numberText.text = numbers[position].toString()

        // Highlight special numbers (e.g., below 10)
        if (numbers[position] < 10) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.lottery_highlight)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )
        }
    }

    override fun getItemCount(): Int {
        return numbers.size
    }

    class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numberText: TextView
        var cardView: MaterialCardView

        init {
            numberText = itemView.findViewById(R.id.number_text)
            cardView = itemView.findViewById(R.id.number_card)
        }
    }
}