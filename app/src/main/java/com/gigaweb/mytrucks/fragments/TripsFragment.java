package com.gigaweb.mytrucks.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gigaweb.mytrucks.Constants;
import com.gigaweb.mytrucks.MapApiInterface;
import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.Result;
import com.gigaweb.mytrucks.adapters.TripAdapter;
import com.gigaweb.mytrucks.models.Trip;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class TripsFragment extends Fragment implements OnItemClickListener {

    private TripAdapter adapter;
    private List<Trip> trips;
    private RecyclerView recyclerView;
    private Trip trip;
    private ProgressDialog progressDialog;
    private JSONObject object;
    private NavController navController;
    private MapApiInterface mapApiInterface;
    private String distance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        navController = Navigation.findNavController(view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        trips = new ArrayList<>();
        adapter = new TripAdapter();

        getTrips();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/")
                .build();

        mapApiInterface = retrofit.create(MapApiInterface.class);
        getDistance("12.0022"+","+"8.5920", "6.5244"+","+"3.3792");

    }

    private void getDistance(String origin, String destination){
        mapApiInterface.getDistace(Constants.API_KEY, origin, destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Result result) {
                        //String distance = result.getRows().get(0).getElements().get(0).getDistance().getText();
                        Toast.makeText(getContext(), result.getStatus(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getTrips(){

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_TRIPS, response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i< array.length(); i++){
                    object = array.getJSONObject(i);

                    String id = object.getString("id");
                    String date = object.getString("date");
                    String tripNumber = object.getString("trip_no");
                    String truckNumber = object.getString("truck_no");
                    String containerNumber = object.getString("container_no");
                    String goods = object.getString("goods");
                    String origin = object.getString("origin");
                    String destination = object.getString("destination");
                    String eta = object.getString("eta");
                    String distance = object.getString("distance");
                    int fuelConsumption = object.getInt("fuel_consumption");
                    int issues = object.getInt("issues");
                    String status = object.getString("status");

                    Trip trip = new Trip(Integer.parseInt(id), date, tripNumber, truckNumber, containerNumber, goods, origin, destination, eta, distance, fuelConsumption, issues, status);
                    trips.add(trip);
                }

            }catch (Exception e){

            }
            adapter.setTrips(trips);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this::onItemClick);
            progressDialog.dismiss();
        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    @Override
    public void onItemClick(int positions) {
        navController.navigate(R.id.action_tripsFragment_to_tripDetailsFragment);
    }
}
