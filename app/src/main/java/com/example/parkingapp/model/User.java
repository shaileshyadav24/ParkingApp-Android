package com.example.parkingapp.model;

public class User {

    String name;
    String email;
    String contactNumber;
    String carPlateNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public User(String name, String email, String contactNumber, String carPlateNumber) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.carPlateNumber = carPlateNumber;
    }

    public User() {
        this.name = "";
        this.email = "";
        this.contactNumber = "";
        this.carPlateNumber = "";
    }
}
