package com.gigaweb.mytrucks.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Trip;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> trips;
    private NavController navController;
    private Bundle bundle = new Bundle();

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TripAdapter.TripViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    class TripViewHolder extends RecyclerView.ViewHolder{

        private TextView trip_no, truck_no, container_no, origin, destination, eta, distance, fuelConsumption, issues;

        public TripViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            trip_no = itemView.findViewById(R.id.txt_trip_no);
            origin = itemView.findViewById(R.id.txt_origin);
            destination = itemView.findViewById(R.id.txt_destination);
            eta = itemView.findViewById(R.id.txt_eta);
            distance = itemView.findViewById(R.id.txt_distance);
            fuelConsumption = itemView.findViewById(R.id.txt_fuel_consumption);
            issues = itemView.findViewById(R.id.txt_issues);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }

        public void bind(Trip trip){
            trip_no.setText(trip.getTripNumber());
            origin.setText(trip.getOrigin());
            destination.setText(trip.getDestination());
            eta.setText(trip.getEta().toString());
            distance.setText(trip.getDistance()+" km");
            fuelConsumption.setText(Integer.toString(trip.getFuelConsumption()));
            issues.setText(Integer.toString(trip.getIssues()));
        }
    }
}
