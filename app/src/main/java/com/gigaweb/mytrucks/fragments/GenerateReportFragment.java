package com.gigaweb.mytrucks.fragments;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GenerateReportFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private TextInputEditText startDate;
    private TextInputEditText endDate;
    private String type;
    private Button btn_generate;
    private FloatingActionButton fab;
    private DownloadManager downloadManager;
    private ProgressDialog progressDialog;
    private String start_date;
    private String end_date;
    private String day;
    private String month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_generate_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        String  prevDest = Navigation.findNavController(view).getPreviousBackStackEntry().getDestination().getLabel().toString();

        startDate = view.findViewById(R.id.editText_startDate);
        endDate = view.findViewById(R.id.editText_endDate);
        btn_generate = view.findViewById(R.id.btn_generate);
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prevDest.matches("Transactions")){
                    //generateReport(start_date, end_date);
                }

            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), R.style.Dialog_Theme, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (type == "start"){
                    startDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
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
                    start_date = year+"-"+month+"-"+day;

                }else {
                    endDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
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
                    end_date = year+"-"+month+"-"+day;
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "start";
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "end";
                datePickerDialog.show();
            }
        });
    }

    public void generateTruckReport(String truckNumber, String startDate, String endDate){

        String truck_number = getArguments().getString("truck_number");
        String pdfurl = Constants.ROOT_URL+"/reports/"+truck_number+" report.pdf";

        progressDialog.setMessage("Generating Report...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GENERATE_REPORT, response -> {
            progressDialog.dismiss();
            Uri uri = Uri.parse(pdfurl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long refference = downloadManager.enqueue(request);

        },

                error -> {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("truck_number", truckNumber);
                params.put("start_date", startDate);
                params.put("end_date", endDate);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

}