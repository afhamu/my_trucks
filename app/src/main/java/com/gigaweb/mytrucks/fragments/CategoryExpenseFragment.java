package com.gigaweb.mytrucks.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.adapters.CategoryAdapter;
import com.gigaweb.mytrucks.models.Category;
import com.gigaweb.mytrucks.models.ExpenseCategory;
import com.gigaweb.mytrucks.models.Month;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryExpenseFragment extends Fragment {

    private PieChart expensePieChart;
    private ProgressDialog progressDialog;
    private String diesel;
    private String maintenance;
    private String tripAllowance;
    private String others;
    private String total;
    private List<Category> categories;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        adapter = new CategoryAdapter(categories);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        expensePieChart = view.findViewById(R.id.expense_pieChart);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getValues();
        setupPiChart();
    }

    public void getValues(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_CATEGORY_EXPENSE, response -> {
            try {
                JSONArray array = new JSONArray(response);

                for (int i = 0; i< array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    total = object.getString("total");
                    diesel = object.getString("diesel");
                    maintenance = object.getString("maintenance");
                    tripAllowance = object.getString("tripAllowance");
                    others = object.getString("others");

                }

            }catch (Exception e){
                e.printStackTrace();
            }

            int dValue = (Integer.parseInt(diesel)*100)/Integer.parseInt(total);
            int mValue = (Integer.parseInt(maintenance)*100)/Integer.parseInt(total);
            int tValue = (Integer.parseInt(tripAllowance)*100)/Integer.parseInt(total);
            int oValue = (Integer.parseInt(others)*100)/Integer.parseInt(total);

            Category catD = new Category("Diesel", Integer.parseInt(diesel), Integer.toString(dValue));
            Category catM = new Category("Maintenance", Integer.parseInt(maintenance), Integer.toString(mValue));
            Category catT = new Category("Trip Allowance", Integer.parseInt(tripAllowance), Integer.toString(tValue));
            Category catO = new Category("Others", Integer.parseInt(others), Integer.toString(oValue));

            categories.add(catD);
            categories.add(catM);
            categories.add(catT);
            categories.add(catO);
            adapter.setCategories(categories);
            loadPieChart(total, diesel, maintenance, tripAllowance, others);
            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void setupPiChart(){
        expensePieChart.setDrawHoleEnabled(true);
        expensePieChart.setUsePercentValues(true);
        expensePieChart.setEntryLabelTextSize(12);
        expensePieChart.setEntryLabelColor(Color.BLACK);
        expensePieChart.setCenterText("Expenses");
        expensePieChart.setCenterTextSize(24);
        expensePieChart.getDescription().setEnabled(false);
        expensePieChart.setDrawEntryLabels(false);

        Legend legend = expensePieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }
    private void loadPieChart(String total, String diesel, String maintenance, String tripAllowance, String others){
        List<PieEntry> entries = new ArrayList<>();
        int dValue = (Integer.parseInt(diesel)*100)/Integer.parseInt(total);
        int mValue = (Integer.parseInt(maintenance)*100)/Integer.parseInt(total);
        int tValue = (Integer.parseInt(tripAllowance)*100)/Integer.parseInt(total);
        int oValue = (Integer.parseInt(others)*100)/Integer.parseInt(total);
        entries.add(new PieEntry(dValue, "Diesel"));
        entries.add(new PieEntry(mValue, "Maintenance"));
        entries.add(new PieEntry(tValue, "Trip Allowance"));
        entries.add(new PieEntry(oValue, "Others"));
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(expensePieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        expensePieChart.setData(data);
        expensePieChart.invalidate();
    }


}