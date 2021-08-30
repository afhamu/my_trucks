package com.gigaweb.mytrucks.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.adapters.MonthAdapter;
import com.gigaweb.mytrucks.models.Month;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class MonthlyFragment extends Fragment {

    private MonthAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private String monthName;
    private String monthNumber;
    private String monthIncome;
    private String monthExpense;
    private List<Month> months;
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        months = new ArrayList<>();
        List<String> years = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner);
        progressDialog = new ProgressDialog(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.monthly_all_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MonthAdapter();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"));
        int startYear = 2018;
        int endYear = calendar.get(Calendar.YEAR);

        for (int year = startYear; year <= endYear; year++) {
            years.add(Integer.toString(year));
        }

        spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, years);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getPosition(Integer.toString(endYear)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getMonthTransactions(years.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getMonthTransactions(String year){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_MONTHLY_ALL, response -> {
            try {
                JSONArray array = new JSONArray(response);
                months.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    monthNumber = object.getString("month_number");
                    monthExpense = object.getString("month_expenses");
                    monthIncome = object.getString("month_income");

                    switch (Integer.parseInt(monthNumber)){
                        case 1:
                            monthName = "January";
                            break;
                        case 2:
                            monthName = "February";
                            break;
                        case 3:
                            monthName = "March";
                            break;
                        case 4:
                            monthName = "April";
                            break;
                        case 5:
                            monthName = "May";
                            break;
                        case 6:
                            monthName = "June";
                            break;
                        case 7:
                            monthName = "July";
                            break;
                        case 8:
                            monthName = "August";
                            break;
                        case 9:
                            monthName = "September";
                            break;
                        case 10:
                            monthName = "October";
                            break;
                        case 11:
                            monthName = "November";
                            break;
                        case 12:
                            monthName = "December";
                            break;
                    }

                    months.add(new Month(monthName,monthNumber, year, monthExpense, monthIncome));

                }
            } catch (Exception e) {

            }
            adapter.setMonths(months);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();

        },

                error -> {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("year", year);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


}