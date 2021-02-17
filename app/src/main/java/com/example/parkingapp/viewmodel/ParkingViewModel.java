package com.example.parkingapp.viewmodel;

import com.example.parkingapp.model.Parking;
import com.example.parkingapp.repositories.ParkingRepository;
import com.example.parkingapp.repositories.UserRepository;

public class ParkingViewModel {

    private static final ParkingViewModel ourInstance = new ParkingViewModel();
    private final ParkingRepository parkingRepository = new ParkingRepository();

    public static ParkingViewModel getInstance() {
        return ourInstance;
    }

    private ParkingViewModel() {
    }

    public ParkingRepository getParkingRepository() {
        return this.parkingRepository;
    }

    public void getAllParkingList(String id) {
        parkingRepository.getAllParkingList(id);
    }

    public void getParkingInfo(String userId, String parkingId) {
        parkingRepository.getParkingInformation(userId, parkingId);
    }

    public void addNewParking(String userId, Parking parking){
        parkingRepository.addParkingToUser(userId, parking);
    }

    public void deleteParking(String userId, String parkingId) {
        parkingRepository.deleteParkingFromUser(userId, parkingId);
    }
}
