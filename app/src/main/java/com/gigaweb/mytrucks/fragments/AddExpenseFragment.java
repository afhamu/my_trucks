package com.gigaweb.mytrucks.fragments;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Truck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddExpenseFragment extends Fragment {

    private FloatingActionButton fab;
    private NavController navController;
    private AutoCompleteTextView trucks_dropdown;
    private AutoCompleteTextView categories_dropdown;
    private TextInputLayout editText_amount;
    private TextInputLayout editText_description;
    private ArrayAdapter adapter;
    private ArrayList<String> trucks = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private ProgressDialog progressDialog;
    private Button btnAddExpense;
    private TextInputEditText date;
    private String day;
    private String month;
    private String eDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrucksList();
        getExpenseCategory();
        navController = Navigation.findNavController(view);
        progressDialog = new ProgressDialog(getContext());
        date = view.findViewById(R.id.editText_date);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        trucks_dropdown = view.findViewById(R.id.trucks);
        categories_dropdown = view.findViewById(R.id.category);
        editText_amount = view.findViewById(R.id.amount);
        editText_description = view.findViewById(R.id.description);

        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, trucks);
        trucks_dropdown.setAdapter(adapter);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
        categories_dropdown.setAdapter(adapter);

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), R.style.Dialog_Theme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                if (String.valueOf(dayOfMonth).length() == 1){
                    day = "0"+(dayOfMonth);
                }else{
                    day = Integer.toString(dayOfMonth);
                }

                if(String.valueOf(monthOfYear).length() == 1){
                    month = "0"+ (monthOfYear+1);
                }else {
                    month = Integer.toString(monthOfYear);
                }
                eDate = year+"-"+month+"-"+day;
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String truck = trucks_dropdown.getText().toString();
                String category = categories_dropdown.getText().toString();
                String date = eDate;
                String amount = editText_amount.getEditText().getText().toString();
                String description = editText_description.getEditText().getText().toString();
                addEpense(date, truck, category, amount, description);
            }
        });


    }

    public void addEpense(String date, String truck, String category, String amount, String description){
        progressDialog.setMessage("Adding...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_EXPENSE, response -> {
            progressDialog.dismiss();
            navController.navigate(R.id.action_addExpenseFragment_to_mainFragment);
        },

                error -> {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("truck_number", truck);
                params.put("expense_type", category);
                params.put("cost", amount);
                params.put("desc", description);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public void getTrucksList(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i< array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    String truckNumber = object.getString("truck_number");
                    trucks.add(truckNumber);

                }
            }catch (Exception e){

            }

            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public void getExpenseCategory(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_EXPENSE_CATEGORIES, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i< array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    String category = object.getString("name");
                    categories.add(category);

                }
            }catch (Exception e){

            }

            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }
}