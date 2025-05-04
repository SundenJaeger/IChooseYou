package com.android.ichooseyou.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ichooseyou2.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LotteryNumberAdapter extends RecyclerView.Adapter<LotteryNumberAdapter.NumberViewHolder> {

    private List<Integer> numbers;

    public LotteryNumberAdapter(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public void updateNumbers(List<Integer> newNumbers) {
        this.numbers = newNumbers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lottery_number, parent, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.numberText.setText(String.valueOf(numbers.get(position)));

        // Highlight special numbers (e.g., below 10)
        if (numbers.get(position) < 10) {
            holder.cardView.setCardBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(R.color.lottery_highlight));
        } else {
            holder.cardView.setCardBackgroundColor(
                    holder.itemView.getContext().getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    static class NumberViewHolder extends RecyclerView.ViewHolder {
        TextView numberText;
        MaterialCardView cardView;

        NumberViewHolder(View itemView) {
            super(itemView);
            numberText = itemView.findViewById(R.id.number_text);
            cardView = itemView.findViewById(R.id.number_card);
        }
    }
}