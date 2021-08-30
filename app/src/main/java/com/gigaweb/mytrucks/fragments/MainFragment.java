package com.gigaweb.mytrucks.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.VechiclesActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.gigaweb.mytrucks.R.drawable.ic_visibility_off;

import java.util.List;

public class MainFragment extends Fragment {

    private TextView textView_total_expenses, textView_total_income, textView_net_profit,
            textView_total_trucks, textView_total_enroute, textView_total_idle, textView_total_stopped;
    private ProgressDialog progressDialog;
    private String total_expenses;
    private String total_income;
    private NavigationView add_transaction_nav;
    private NavController navController;
    private String total_trucks, total_enroute, total_idle, total_stopped;
    private ImageView toogle;
    private ConstraintLayout completed, ongoing, delayed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();


        navController = Navigation.findNavController(view);
        progressDialog = new ProgressDialog(getActivity());

        textView_total_income = view.findViewById(R.id.textview_income_total);
        textView_total_expenses = view.findViewById(R.id.textview_expense_total);
        textView_net_profit = view.findViewById(R.id.textview_net_profit);
        textView_total_enroute = view.findViewById(R.id.total_enroute);
        textView_total_idle = view.findViewById(R.id.total_idle);
        textView_total_stopped = view.findViewById(R.id.total_stopped);
        textView_total_trucks = view.findViewById(R.id.txt_total_trucks);
        toogle = view.findViewById(R.id.imageView_toggle);
        completed = view.findViewById(R.id.completed_trips);
        Intent intent = new Intent(getContext(), VechiclesActivity.class);

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_mainFragment_to_tripsFragment);
            }
        });

        toogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView_total_income.getText().toString();
                if (text.contains("*")) {
                    getTruckDetails("show");
                    toogle.setImageResource(R.drawable.ic_visibility);
                } else {
                    getTruckDetails("hide");
                    toogle.setImageResource(ic_visibility_off);
                }

            }
        });

        textView_total_enroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("position", "En route");
                startActivity(intent);
            }
        });

        textView_total_idle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("position", "Idle");
                startActivity(intent);
            }
        });

        textView_total_stopped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("position", "Stopped");
                startActivity(intent);
            }
        });

        getTruckDetails("hide");
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_options:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_transaction, getView().findViewById(R.id.bottomSheetContainer));
                add_transaction_nav = bottomSheetView.findViewById(R.id.add_transaction_nav);
                add_transaction_nav.setItemIconTintList(null);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                add_transaction_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_income:
                                navController.navigate(R.id.action_mainFragment_to_addIncomeFragment);
                                bottomSheetDialog.dismiss();
                                return true;
                            case R.id.add_expense:
                                navController.navigate(R.id.action_mainFragment_to_addExpenseFragment);
                                bottomSheetDialog.dismiss();
                                return true;
                            case R.id.generate_report:
                                navController.navigate(R.id.action_mainFragment_to_generateReportFragment);
                                bottomSheetDialog.dismiss();
                                return true;
                            case R.id.generate_transaction:
                                navController.navigate(R.id.action_mainFragment_to_generateStatementFragment);
                                bottomSheetDialog.dismiss();
                                return true;
                        }
                        return false;
                    }
                });

        }
        return super.onOptionsItemSelected(item);

    }

    public void getTruckDetails(String visibility) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    total_income = object.getString("income");
                    total_expenses = object.getString("expenses");
                    total_enroute = object.getString("total_en_route");
                    total_idle = object.getString("total_idle");
                    total_stopped = object.getString("total_stopped");
                    total_trucks = object.getString("total");

                }
            } catch (Exception e) {

            }

            if (visibility.matches("show")) {
                textView_total_expenses.setText("\u20A6" + total_expenses);
                textView_total_income.setText("\u20A6" + total_income);
            } else {
                textView_total_income.setText("\u20A6" + "********");
                textView_total_expenses.setText("\u20A6" + "********");
            }

            if (total_stopped.length() == 1){
                textView_total_stopped.setText("0"+total_stopped);
            }else {
                textView_total_stopped.setText(total_stopped);
            }

            if (total_enroute.length() == 1){
                textView_total_enroute.setText("0"+total_enroute);
            }else {
                textView_total_enroute.setText(total_enroute);
            }

            if (total_idle.length() == 1){
                textView_total_idle.setText("0"+total_idle);
            }else {
                textView_total_idle.setText(total_idle);
            }

            textView_total_trucks.setText(total_trucks);
            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

}