package com.gigaweb.mytrucks.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.VechiclesActivity;
import com.gigaweb.mytrucks.adapters.TruckAdapter;
import com.gigaweb.mytrucks.adapters.VehicleAdapter;
import com.gigaweb.mytrucks.models.Truck;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import me.relex.circleindicator.CircleIndicator2;


public class VehiclesFragment extends Fragment implements OnItemClickListener {

    public static final String EXTRA_TRUCK_NUMBER = "truckNumber";
    public static final String EXTRA_MAKE = "make";
    public static final String EXTRA_DRIVER = "driver";
    public static final String EXTRA_DRIVER_PHONE = "driver_phone";
    public static final String EXTRA_TOTAL_INCOME = "total_income";
    public static final String EXTRA_TOTAL_EXPENSES = "total_expenses";
    public static final String EXTRA_SAVED = "saved";


    private RecyclerView recyclerView;
    private List<Truck> trucks;
    private VehicleAdapter adapter;
    private ProgressDialog progressDialog;
    private Truck truck;
    private String truckNumber, make, driver, position, driverPhone, totalIncome, totalExpenses, status, state;
    private NavController navController;
    private SearchView searchView;
    private String truckPosition;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intent = getActivity().getIntent();
        truckPosition = intent.getStringExtra("position");
        //Toast.makeText(getContext(), truckPosition, Toast.LENGTH_LONG).show();
        searchView = view.findViewById(R.id.searchView);
        navController = Navigation.findNavController(view);
        trucks = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        getTruckDetails();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VehicleAdapter(trucks);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(VehiclesFragment.this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilteredResults(newText);
                return true;
            }

        });


    }

    public void getTruckDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i< array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    truckNumber = object.getString("truck_number");
                    make = object.getString("make");
                    driver = object.getString("driver");
                    driverPhone = object.getString("driver_phone_number");
                    totalExpenses = object.getString("total_expenses");
                    totalIncome = object.getString("total_income");
                    position = object.getString("position");
                    state = object.getString("state");
                    //status = object.getString("status");

                    truck = new Truck(truckNumber, make, driver, position, driverPhone, totalExpenses, totalIncome, state);

                    if(intent.getStringExtra("position") != null){
                        if(truckPosition.matches(position)){
                            trucks.add(truck);
                        }
                    }else{
                        trucks.add(truck);
                    }

                }
            }catch (Exception e){

            }

            adapter.setTrucks(trucks);
            progressDialog.dismiss();

        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    @Override
    public void onItemClick(int position) {
        Truck clickedTruck = trucks.get(position);
        Bundle bundle = new Bundle();
        String totalIncome;
        String totalExpenses;
        String saved;
        bundle.putString(EXTRA_TRUCK_NUMBER, clickedTruck.getTruckNumber());
        bundle.putString(EXTRA_MAKE, clickedTruck.getMake());
        bundle.putString(EXTRA_DRIVER, clickedTruck.getDriver());
        bundle.putString(EXTRA_DRIVER_PHONE, clickedTruck.getDriverPhone());
        bundle.putString("position", clickedTruck.getPosition());
        if (clickedTruck.getTotalIncome().matches("null")){
            totalIncome = "0";
        }else{
            totalIncome = clickedTruck.getTotalIncome();
        }

        if (clickedTruck.getTotalExpenses().matches("null")){
            totalExpenses = "0";
        }else{
            totalExpenses = clickedTruck.getTotalExpenses();
        }
        bundle.putString(EXTRA_TOTAL_INCOME, totalIncome);
        bundle.putString(EXTRA_TOTAL_EXPENSES, totalExpenses);

        if(clickedTruck.getTotalIncome().matches("null") && clickedTruck.getTotalExpenses().matches("null")){
            saved = "0";
        }else if(clickedTruck.getTotalIncome().matches("null") && !clickedTruck.getTotalExpenses().matches("null")){
            int intSaved = 0-Integer.parseInt(clickedTruck.getTotalExpenses());
            saved = Integer.toString(intSaved);
        }else if (!clickedTruck.getTotalIncome().matches("null") && clickedTruck.getTotalExpenses().matches("null")){
            int intSaved = Integer.parseInt(clickedTruck.getTotalIncome())-0;
            saved = Integer.toString(intSaved);
        }else{
            int intSaved = Integer.parseInt(clickedTruck.getTotalIncome())-Integer.parseInt(clickedTruck.getTotalExpenses());
            saved = Integer.toString(intSaved);
        }
        bundle.putString(EXTRA_SAVED, saved);
        bundle.putString("title", clickedTruck.getTruckNumber());
       // NavDirections directions = VehiclesFragmentDirections.actionVehiclesFragmentToVehicleDetailsFragment(clickedTruck.getTruckNumber());
        navController.navigate(R.id.action_vehiclesFragment_to_vehicleDetailsFragment, bundle);


    }

    public void getFilteredResults(String constraint) {
        List<Truck> results = new ArrayList<>();

        for (Truck item : trucks) {
            if (item.getTruckNumber().toLowerCase().contains(constraint) || item.getTruckNumber().toUpperCase().contains(constraint)) {
                results.add(item);
            }
        }
        adapter.filterlist(results);
    }
}