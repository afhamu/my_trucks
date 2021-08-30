package com.gigaweb.mytrucks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Category;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Locale locale = new Locale("en", "NG");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        Category category = categories.get(position);
        holder.textView_categoryName.setText(category.getCategoryName()+" ("+ category.getPercentageVal()+"%)");
        holder.textView_amount.setText(numberFormat.format(category.getAmount()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView_categoryName;
        private final TextView textView_amount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_categoryName = itemView.findViewById(R.id.category);
            textView_amount = itemView.findViewById(R.id.amount);
        }
    }
}
