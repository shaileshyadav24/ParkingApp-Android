package com.example.parkingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.R;
import com.example.parkingapp.activites.ui.SavedParking.OnClickListener;
import com.example.parkingapp.activites.ui.SavedParking.OnLongClickListener;
import com.example.parkingapp.model.Parking;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingistViewHolder> {
    private Context context;
    private ArrayList<Parking> parkingList;
    private OnLongClickListener onLongClickListener;
    private OnClickListener onClickListener;


    public ParkingAdapter(Context context, ArrayList<Parking> checklists, OnLongClickListener onLongClickListener, OnClickListener onClickListener) {
        this.context = context;
        this.parkingList = checklists;
        this.onLongClickListener = onLongClickListener;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ParkingistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parking_item, null, false);
        return new ParkingistViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ParkingistViewHolder holder, int position) {
        holder.bind(parkingList.get(position), this.onLongClickListener, this.onClickListener);
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public static class ParkingistViewHolder extends RecyclerView.ViewHolder {
        TextView carPlate;
        TextView time;
        TextView building;
        TextView suite;
        TextView hours;
        LinearLayout layout;

        public ParkingistViewHolder(@NonNull View itemView) {
            super(itemView);
            carPlate = itemView.findViewById(R.id.displayCarPlate);
            time = itemView.findViewById(R.id.displayTime);
            building = itemView.findViewById(R.id.displayBuildingCode);
            suite = itemView.findViewById(R.id.displaySuiteNo);
            hours = itemView.findViewById(R.id.displayHours);
            layout = itemView.findViewById(R.id.parkingLayout);
        }

        public void bind(Parking parking, OnLongClickListener onLongClickListener, OnClickListener onClickListener) {
            carPlate.setText("" + parking.getPlateNumber());
            Log.e("TIME", parking.getPlateNumber());
            time.setText("Time: " + parking.getTime());
            building.setText("Building: " + parking.getBuildingCode());
            suite.setText("Suite: " + parking.getSuiteNo());
            hours.setText("Hours: " + parking.getHours());

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.onLongClickListener(parking);
                    return false;
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickListener(parking);
                }
            });

        }


    }
}

