package com.example.parkingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.R;
import com.example.parkingapp.activites.ui.SavedParking.OnLongClickListener;
import com.example.parkingapp.model.Parking;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ChecklistViewHolder> {
    private Context context;
    private ArrayList<Parking> parkingList;
    private OnLongClickListener onLongClickListener;


    public ParkingAdapter(Context context, ArrayList<Parking> checklists, OnLongClickListener onLongClickListener) {
        this.context = context;
        this.parkingList = checklists;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ParkingAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parking_item, null, false);
        return new ChecklistViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        holder.bind(parkingList.get(position), this.onLongClickListener);
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        TextView carPlate;
        TextView time;
        TextView building;
        TextView suite;
        TextView hours;
        LinearLayout layout;

        public ChecklistViewHolder(@NonNull View itemView) {
            super(itemView);
            carPlate = itemView.findViewById(R.id.displayCarPlate);
            time = itemView.findViewById(R.id.displayTime);
            building = itemView.findViewById(R.id.displayBuildingCode);
            suite = itemView.findViewById(R.id.displaySuiteNo);
            hours = itemView.findViewById(R.id.displayHours);
            layout = itemView.findViewById(R.id.parkingLayout);
        }

        public void bind(Parking parking, OnLongClickListener onLongClickListener) {
            carPlate.setText(parking.getPlateNumber());
            time.setText(parking.getTime());
            building.setText(parking.getBuildingCode());
            suite.setText(parking.getSuiteNo());
            hours.setText(parking.getHours());

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.onLongClickListener(parking);
                    return false;
                }
            });
        }


    }
}

