package com.gigaweb.mytrucks.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.adapters.CategoryAdapter;
import com.gigaweb.mytrucks.models.Category;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryIncomeFragment extends Fragment {
    private PieChart incomePieChart;
    private ProgressDialog progressDialog;
    private String total;
    private String importVal;
    private String exportVal;
    private String othersVal;
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
        return inflater.inflate(R.layout.fragment_category_income, container, false);
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
        incomePieChart = view.findViewById(R.id.income_pieChart);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getValues();
        setupPiChart();
    }

    public void getValues(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_CATEGORY_INCOME, response -> {
            try {
                JSONArray array = new JSONArray(response);

                for (int i = 0; i< array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    total = object.getString("total");
                    importVal = object.getString("import");
                    exportVal = object.getString("export");
                    othersVal = object.getString("others");

                }

            }catch (Exception e){
                e.printStackTrace();
            }

            int iValue = (Integer.parseInt(importVal)*100)/Integer.parseInt(total);
            int eValue = (Integer.parseInt(exportVal)*100)/Integer.parseInt(total);
            int oValue = (Integer.parseInt(othersVal)*100)/Integer.parseInt(total);

            Category catI = new Category("Import", Integer.parseInt(importVal), Integer.toString(iValue));
            Category catE = new Category("Export", Integer.parseInt(exportVal), Integer.toString(eValue));
            Category catO = new Category("Others", Integer.parseInt(othersVal), Integer.toString(oValue));

            categories.add(catI);
            categories.add(catE);
            categories.add(catO);
            adapter.setCategories(categories);

            loadPieChart(total, importVal, exportVal, othersVal);
            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void setupPiChart(){
        incomePieChart.setDrawHoleEnabled(true);
        incomePieChart.setUsePercentValues(true);
        incomePieChart.setEntryLabelTextSize(12);
        incomePieChart.setEntryLabelColor(Color.BLACK);
        incomePieChart.setCenterText("Income");
        incomePieChart.setCenterTextSize(24);
        incomePieChart.getDescription().setEnabled(false);
        incomePieChart.setDrawEntryLabels(false);

        Legend legend = incomePieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void loadPieChart(String total, String importVal, String exportVal, String othersVal){
        ArrayList<PieEntry> entries = new ArrayList<>();
        int iValue = (Integer.parseInt(importVal)*100)/Integer.parseInt(total);
        int eValue = (Integer.parseInt(exportVal)*100)/Integer.parseInt(total);
        int oValue = (Integer.parseInt(othersVal)*100)/Integer.parseInt(total);

        entries.add(new PieEntry(iValue, "Import"));
        entries.add(new PieEntry(eValue, "Export"));
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
        data.setValueFormatter(new PercentFormatter(incomePieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        incomePieChart.setData(data);
        incomePieChart.invalidate();
    }

}