// Created by Shailesh Yadav 101332535
package com.example.parkingapp.activites;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.parkingapp.R;
import com.example.parkingapp.model.Parking;
import com.example.parkingapp.viewmodel.ParkingViewModel;
import com.example.parkingapp.viewmodel.UserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ParkingViewModel parkingViewModel;
    private UserViewModel userViewModel;
    private final Float DEFAULT_ZOOM = 15.0f;
    Parking parking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parkingViewModel = ParkingViewModel.getInstance();
        userViewModel = UserViewModel.getInstance();

        parking = (Parking) getIntent().getSerializableExtra("parking");


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng parkingLocation = new LatLng(parking.getLatitude(), parking.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parkingLocation, DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions().position(parkingLocation).title("Parking location"));

        if(googleMap != null) {
            this.setUpGoogleMapSettings(googleMap);
        }
    }


    private void setUpGoogleMapSettings(GoogleMap googleMap) {
        googleMap.setBuildingsEnabled(true);
        googleMap.setTrafficEnabled(false);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

    }
}