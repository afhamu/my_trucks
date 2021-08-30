package com.gigaweb.mytrucks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;

import org.jetbrains.annotations.NotNull;

public class AdminDashboardFragment extends Fragment implements View.OnClickListener {

    private CardView trucks, trips, income, expenses;
    private NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trips = view.findViewById(R.id.trips_cardview);
        trucks = view.findViewById(R.id.trucks_cardview);
        income = view.findViewById(R.id.income_cardview);
        expenses = view.findViewById(R.id.expenses_cardview);
        navController = Navigation.findNavController(view);

        trucks.setOnClickListener(this::onClick);
        trips.setOnClickListener(this::onClick);
        income.setOnClickListener(this::onClick);
        expenses.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.trucks_cardview:
                navController.navigate(R.id.action_adminDashboardFragment_to_vehiclesFragment);
                break;
            case R.id.trips_cardview:
                navController.navigate(R.id.action_adminDashboardFragment_to_tripsFragment);
                break;
        }
    }
}