package com.gigaweb.mytrucks.fragments;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.adapters.TruckAdapter;
import com.gigaweb.mytrucks.models.Truck;

import java.util.List;


public class VehicleDetailsFragment extends Fragment {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1234;
    private TextView textView_status;
    private TextView textView_total_income;
    private TextView textView_total_expenses;
    private TextView textView_saved;
    private TextView textView_position;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView_status = view.findViewById(R.id.textview_status);
        textView_total_income = view.findViewById(R.id.textview_truck_income_value);
        textView_total_expenses = view.findViewById(R.id.textview_truck_expenses_value);
        textView_saved = view.findViewById(R.id.textview_saved);
        textView_position = view.findViewById(R.id.textView_position);
        textView_total_income.setText("\u20A6"+getArguments().getString("total_income"));
        textView_total_expenses.setText("\u20A6"+getArguments().getString("total_expenses"));
        textView_saved.setText("\u20A6"+getArguments().getString("saved"));
        textView_position.setText(getArguments().getString("position"));

        String saved = getArguments().getString("saved");
        if(saved.substring(0,1).matches("-")){
            textView_saved.setTextColor(Color.parseColor("#ff0000"));
        }
        
    }
}