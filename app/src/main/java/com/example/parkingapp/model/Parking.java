package com.example.parkingapp.model;

import java.io.Serializable;

public class Parking  implements Serializable  {
    String id = "";
    String buildingCode;
    String hours;
    String plateNumber;
    String suiteNo;
    Double latitude;
    Double longitude;
    String time;


    public Parking(String buildingCode, String hours, String plateNumber, String suiteNo, Double latitude, Double longitude, String time) {
        this.buildingCode = buildingCode;
        this.hours = hours;
        this.plateNumber = plateNumber;
        this.suiteNo = suiteNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public Parking() {
        this.buildingCode = "";
        this.hours = "";
        this.plateNumber = "";
        this.suiteNo = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.time = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getSuiteNo() {
        return suiteNo;
    }

    public void setSuiteNo(String suiteNo) {
        this.suiteNo = suiteNo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
