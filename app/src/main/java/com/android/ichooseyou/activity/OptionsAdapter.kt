package com.android.ichooseyou.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.ichooseyou.activity.OptionsAdapter.OptionViewHolder
import com.android.ichooseyou2.R
class OptionsAdapter(private val options: List<String>) : RecyclerView.Adapter<OptionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_option, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionText.text = options[position]
    }

    override fun getItemCount(): Int {
        return options.size
    }

    class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionText: TextView

        init {
            optionText = itemView.findViewById(R.id.option_text)
        }
    }
}