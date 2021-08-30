package com.gigaweb.mytrucks.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gigaweb.mytrucks.OnItemClickListener;
import com.gigaweb.mytrucks.R;
import com.gigaweb.mytrucks.models.Truck;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Truck> trucks;
    private List<Truck> filteredList = new ArrayList<>();
    private static final int CALL_PERMISSION_REQUEST_CODE = 1234;
    private String driver_phone;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public VehicleAdapter(List<Truck> trucks) {
        this.trucks = trucks;
        this.filteredList = trucks;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Truck currentTruck = trucks.get(position);
        holder.bind(currentTruck);

    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
        notifyDataSetChanged();
    }

    public void filterlist(List<Truck> trucks) {
        this.trucks = trucks;
        notifyDataSetChanged();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView_truckNumber;
        private final TextView textView_position_circle;
        private final TextView textView_driver;
        private final TextView textView_location;

        private final ImageView call_driver;


        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_truckNumber = itemView.findViewById(R.id.textView_trip_number);
            textView_position_circle = itemView.findViewById(R.id.position_circle);
            textView_driver = itemView.findViewById(R.id.textView_driver_name);
            call_driver = itemView.findViewById(R.id.imageView_call);
            textView_location = itemView.findViewById(R.id.textView_location);

            call_driver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (ActivityCompat.checkSelfPermission(v.getContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) v.getContext().getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE},
                                CALL_PERMISSION_REQUEST_CODE);
                    }else {
                        callIntent.setData(Uri.parse("tel:"+driver_phone));
                        v.getContext().getApplicationContext().startActivity(callIntent);

                    }
                }
            });


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

        public void bind(Truck truck){
            textView_truckNumber.setText(truck.getTruckNumber()+" ["+truck.getMake()+"]");
            textView_driver.setText(truck.getDriver());
            driver_phone = truck.getDriverPhone();
            textView_location.setText(truck.getCity());
            Drawable bg = textView_position_circle.getBackground();
            if (truck.getPosition().matches("Stopped")){
                bg.setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.ADD);
                textView_position_circle.setText("Stopped");
            }else if (truck.getPosition().matches("Idle")){
                bg.setColorFilter(Color.parseColor("#1c9eb0"), PorterDuff.Mode.ADD);
                textView_position_circle.setText("Parked");
            }else {
                bg.setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.ADD);
                textView_position_circle.setText("Moving");
            }
        }
    }
}



