package com.example.parkingapp.activites.ui.SavedParking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class SavedParkingFragment extends Fragment implements OnLongClickListener {

    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private ParkingAdapter parkingAdapter;
    private ArrayList<Parking> parkings;
    private String userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parkingViewModel = ParkingViewModel.getInstance();
        userViewModel = UserViewModel.getInstance();
        userId = userViewModel.getUserRepository().userId.getValue();

        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        recyclerView = root.findViewById(R.id.savedParkingList);
        parkings = new ArrayList<Parking>();
        parkingAdapter = new ParkingAdapter(getContext(), parkings, this);
        recyclerView.setAdapter(parkingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

//        this.ckModel.getCkRepository().chkLists.observe(this, new Observer<List<Checklist>>() {
//            @Override
//            public void onChanged(List<Parking> checklists) {
//                if (checklists != null) {
////                    cklist.addAll(checklists);
//                    parkingAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        return root;
    }

    @Override
    public void onLongClickListener(Parking parking) {

    }
}