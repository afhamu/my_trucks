package com.gigaweb.mytrucks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Recent;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentViewHolder> {

    private List<Recent> recent_activities;

    public RecentAdapter(List<Recent> recent_activities) {
        this.recent_activities = recent_activities;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        Locale locale = new Locale("en", "NG");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        Recent recent = recent_activities.get(position);
        holder.textView_category.setText(recent.getCategory());
        holder.textView_date.setText(recent.getDate());
        holder.textView_amount.setText(numberFormat.format(recent.getAmount()));
        if (recent.getActivity().matches("Expense")){
            holder.imageView.setImageResource(R.drawable.down_24px);
        }else {
            holder.imageView.setImageResource(R.drawable.up_24px);
        }
    }

    @Override
    public int getItemCount() {
        return recent_activities.size();
    }

    public void setRecent_activities(List<Recent> recent_activities){
        this.recent_activities = recent_activities;
        notifyDataSetChanged();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView_category;
        private final TextView textView_date;
        private final TextView textView_amount;
        private final ImageView imageView;

        public RecentViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_amount = itemView.findViewById(R.id.textview_amount);
            textView_category = itemView.findViewById(R.id.textview_category);
            textView_date = itemView.findViewById(R.id.textview_date);
            imageView = itemView.findViewById(R.id.imageView_expense_income);
        }
    }
}
