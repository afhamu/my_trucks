package com.gigaweb.mytrucks.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Truck;

import java.util.List;

public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.TruckViewHolder>{

    private List<Truck> trucks;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public TruckAdapter(List<Truck> trucks) {
        this.trucks = trucks;
    }

    @NonNull
    @Override
    public TruckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.truck, parent, false);
        return new TruckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruckViewHolder holder, int position) {
            Truck currentTruck = trucks.get(position);
            holder.textView_truckNumber.setText(currentTruck.getTruckNumber()+" ["+currentTruck.getMake()+"]");
            Bundle bundle = new Bundle();
            bundle.putString("truck_number", currentTruck.getTruckNumber());

    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
        notifyDataSetChanged();
    }

    public class TruckViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView_truckNumber;
        private final TextView textView_truck_income;
        private final TextView textView_truck_expenses;
        private final TextView textView_saved;



        public TruckViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_truckNumber = itemView.findViewById(R.id.textView_trip_number);
            textView_truck_income = itemView.findViewById(R.id.textview_truck_income_value);
            textView_truck_expenses = itemView.findViewById(R.id.textview_truck_expenses_value);
            textView_saved = itemView.findViewById(R.id.textview_saved);
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
    }
}
