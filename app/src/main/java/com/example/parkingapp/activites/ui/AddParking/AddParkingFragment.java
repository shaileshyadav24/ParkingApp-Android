// Created by Shailesh Yadav 101332535
package com.example.parkingapp.activites.ui.AddParking;

import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.parkingapp.R;
import com.example.parkingapp.manager.LocationManager;
import com.example.parkingapp.model.Parking;
import com.example.parkingapp.model.User;
import com.example.parkingapp.viewmodel.ParkingViewModel;
import com.example.parkingapp.viewmodel.UserViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.view.View.GONE;

public class AddParkingFragment extends Fragment implements View.OnClickListener {

    private EditText buildingCode;
    private String hoursSelected;
    private EditText carPlateNumber;
    private EditText suiteNo;
    private EditText parkingLocation;
    private TextView parkingTimeSelection;
    private Button parkingTimeSelectionBtn;
    private CheckBox myLocation;
    private Button addNewParkingBtn;
    private LocationManager locationManager;
    private LatLng location;
    private LocationCallback locationCallback;
    private TextView errorMessage;
    private String timeSelected;

    private UserViewModel userViewModel;
    private ParkingViewModel parkingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_new, container, false);

        userViewModel = UserViewModel.getInstance();
        parkingViewModel = ParkingViewModel.getInstance();

        buildingCode = root.findViewById(R.id.addBuildingCode);
        carPlateNumber = root.findViewById(R.id.addPlateNumber);
        suiteNo = root.findViewById(R.id.addSuiteNo);
        parkingLocation = root.findViewById(R.id.addParkingLocation);
        errorMessage = root.findViewById(R.id.addErrorMessage);

        myLocation = root.findViewById(R.id.addMyLocation);
        myLocation.setOnClickListener(this);

        addNewParkingBtn = root.findViewById(R.id.addNewParkingBtn);
        addNewParkingBtn.setOnClickListener(this);

        parkingTimeSelection = root.findViewById(R.id.addParkingTime);

        parkingTimeSelectionBtn = root.findViewById(R.id.parkingTimeSelectionBtn);
        parkingTimeSelectionBtn.setOnClickListener(this);

        this.locationManager = LocationManager.getInstance();
        this.locationManager.checkPermissions(getContext());

        userViewModel.getUserRepository().profileInfo.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getCarPlateNumber() != null) {
                    carPlateNumber.setText(user.getCarPlateNumber());
                }
            }
        });

        parkingViewModel.getParkingRepository().parkingStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.contentEquals("SUCCESS")) {
                        errorMessage.setText("Parking added successfully.");
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setTextColor(Color.GREEN);
                    } else {
                        renderErrorMessage(s);
                    }
                }
            }
        });

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

        if (this.locationManager.locationPermissionGranted) {
            this.getLocation();
            Log.e("TAG", "LOCATION GRANTED");
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location loc : locationResult.getLocations()) {
                        location = new LatLng(loc.getLatitude(), loc.getLongitude());
                        Log.e("TAG", "LOCATION FOUND 1: " + loc.getLongitude());
                    }
                }
            };

            this.locationManager.requestLocationUpdates(getContext(), this.locationCallback);
        }

        return root;
    }

    public void renderErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setTextColor(Color.RED);
    }

    public Boolean validateParkingDetails() {
        Boolean flag = true;
        if (buildingCode.getText().toString().isEmpty()) {
            renderErrorMessage("Please enter building code.");
            flag = false;
        } else if (buildingCode.getText().toString().length() > 5) {
            renderErrorMessage("Building code should be of max characters 5.");
            flag = false;
        } else if (hoursSelected == null) {
            renderErrorMessage("Please select number of hours.");
            flag = false;
        } else if (carPlateNumber.getText().toString().length() < 2 || carPlateNumber.getText().toString().length() > 8) {
            renderErrorMessage("Car plate number should be between 2 to 8 characters.");
            flag = false;
        } else if (suiteNo.getText().toString().length() < 2 || suiteNo.getText().toString().length() > 5) {
            renderErrorMessage("Suite number should be between 2 to 5 characters.");
            flag = false;
        } else if (!myLocation.isChecked() && parkingLocation.getText().toString().isEmpty()) {
            renderErrorMessage("Please provide location details");
            flag = false;
        } else if (parkingTimeSelection.getText().toString().isEmpty()) {
            renderErrorMessage("Please select time");
            flag = false;
        }

        return flag;
    }

    public void initiateAddNewParking() {
        errorMessage.setVisibility(GONE);
        if (validateParkingDetails()) {
            LatLng locationToSave;
            if (!myLocation.isChecked()) {
                locationToSave = getLocationFromAddress(parkingLocation.getText().toString());
            } else {
                locationToSave = this.location;
            }
            if (locationToSave != null) {
                Parking parking = new Parking(
                        buildingCode.getText().toString(),
                        hoursSelected,
                        carPlateNumber.getText().toString(),
                        suiteNo.getText().toString(),
                        locationToSave.latitude,
                        locationToSave.longitude,
                        parkingTimeSelection.getText().toString()
                );
                parkingViewModel.addNewParking(userViewModel.getUserRepository().userId.getValue(),
                        parking);
            } else {
                renderErrorMessage("Location not found.");
            }
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception ex) {
        }
        return null;
    }

    public void showSelectedTime(int hour, int min) {
        parkingTimeSelection.setText((hour > 12 ? (hour - 12) : hour) + ":" + (min < 10 ? ("0" + min) : min) + (hour > 12 ? " PM" : " AM"));
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.addNewParkingBtn:
                    initiateAddNewParking();
                    break;
                case R.id.addMyLocation:
                    if (myLocation.isChecked()) {
                        parkingLocation.setText("");
                    }
                    break;
                case R.id.parkingTimeSelectionBtn:
                    final Calendar cldr = Calendar.getInstance();
                    int hour = cldr.get(Calendar.HOUR_OF_DAY);
                    int minutes = cldr.get(Calendar.MINUTE);
                    TimePickerDialog picker = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    showSelectedTime(sHour, sMinute);
                                }
                            }, hour, minutes, false);
                    picker.show();
                    break;
            }
        }
    }

    private void getLocation() {
        this.locationManager.getLastLocation(getContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location loc) {
                if (loc != null) {
                    location = new LatLng(loc.getLatitude(), loc.getLongitude());
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.locationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            this.locationManager.locationPermissionGranted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);

            if (this.locationManager.locationPermissionGranted) {
                //start receiving location and display that on screen
                Log.e("TAG", "LocationPermissionGranted " + this.locationManager.locationPermissionGranted);
            }
            return;
        }
    }

}