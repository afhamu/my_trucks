package com.gigaweb.mytrucks.adapters;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Month;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder>{

    private List<Month> months;
    private NavController navController;
    private Bundle bundle = new Bundle();

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_all_item, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        Month currentMonth = months.get(position);
        holder.bind(currentMonth);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    public void setMonths(List<Month> months) {
        this.months = months;
        notifyDataSetChanged();
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView_month_name;
        private final TextView textView_month_income;
        private final TextView textView_month_expense;
        private final Button btn_report, btn_more;
        private View subItem;

        public MonthViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_month_name = itemView.findViewById(R.id.label_month_name);
            textView_month_income = itemView.findViewById(R.id.label_month_income);
            textView_month_expense = itemView.findViewById(R.id.label_month_expense);
            btn_report = itemView.findViewById(R.id.btn_report);
            btn_more = itemView.findViewById(R.id.btn_more);
            subItem = itemView.findViewById(R.id.sub_item);

        }

        public void bind(Month month){

            boolean expanded = month.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            Locale locale = new Locale("en", "NG");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            textView_month_name.setText(month.getMonthName());
            textView_month_income.setText(numberFormat.format(Integer.parseInt(month.getIncome())));
            textView_month_expense.setText(numberFormat.format(Integer.parseInt(month.getExpenses())));

            btn_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Constants.generateMonthReport(v.getContext(), month.getYear(), month.getMonthName(), month.getMonthNumber());
                }
            });

            btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_transactionMainFragment_to_monthDetailsFragment);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded = month.isExpanded();
                    month.setExpanded(!expanded);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }


}
