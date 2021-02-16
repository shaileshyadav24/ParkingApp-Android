package com.example.parkingapp.activites.ui.SavedParking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingapp.R;
import com.example.parkingapp.adapter.ParkingAdapter;
import com.example.parkingapp.model.Parking;
import com.example.parkingapp.viewmodel.ParkingViewModel;
import com.example.parkingapp.viewmodel.UserViewModel;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.List;

public class SavedParkingFragment extends Fragment implements OnLongClickListener, OnClickListener {

    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private ParkingAdapter parkingAdapter;
    private ArrayList<Parking> parkings;
    private String userId;
    private MapView map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parkingViewModel = ParkingViewModel.getInstance();
        userViewModel = UserViewModel.getInstance();
        userId = userViewModel.getUserRepository().userId.getValue();
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        map = root.findViewById(R.id.mapsDisplay);
        recyclerView = root.findViewById(R.id.savedParkingList);
        parkings = new ArrayList<Parking>();
        parkingAdapter = new ParkingAdapter(getContext(), parkings, this, this);
        recyclerView.setAdapter(parkingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        userViewModel.getUserRepository().userId.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null) {
                    Log.e("DATA", s);
                    parkingViewModel.getAllParkingList(s);
                }
            }
        });

        parkingViewModel.getParkingRepository().parkingList.observe(getViewLifecycleOwner(), new Observer<ArrayList<Parking>>() {
            @Override
            public void onChanged(ArrayList<Parking> p) {
                if (p != null) {
                    parkings.addAll(p);
                    parkingAdapter.notifyDataSetChanged();
                }
            }
        });

        return root;
    }

    @Override
    public void onLongClickListener(Parking parking) {

    }

    @Override
    public void onClickListener(Parking parking) {

    }
}