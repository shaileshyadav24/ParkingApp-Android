package com.example.parkingapp.activites.ui.AddParking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.parkingapp.R;

public class AddParkingFragment extends Fragment implements View.OnClickListener {

    private EditText buildingCode;
    private String hoursSelected;
    private EditText carPlateNumber;
    private EditText suiteNo;
    private EditText parkingLocation;
    private CheckBox myLocation;
    private Button addNewParkingBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        dashboardViewModel =
//                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_new, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        buildingCode = root.findViewById(R.id.addBuildingCode);
        carPlateNumber = root.findViewById(R.id.addPlateNumber);
        suiteNo = root.findViewById(R.id.addSuiteNo);
        parkingLocation = root.findViewById(R.id.addParkingLocation);
        myLocation = root.findViewById(R.id.addMyLocation);
        addNewParkingBtn = root.findViewById(R.id.addNewParkingBtn);
        addNewParkingBtn.setOnClickListener(this);

        Spinner spnHoursSelection = (Spinner) root.findViewById(R.id.addHours);
        final String[] spnTimeList = {"1-hour or less", "4-hour", "12-hour", "24-hour"};
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spnTimeList);
        spnHoursSelection.setAdapter(spnAdapter);
        spnHoursSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hoursSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        if(view != null) {
            switch (view.getId()) {
                case R.id.addNewParkingBtn:
                    break;
            }
        }
    }
}