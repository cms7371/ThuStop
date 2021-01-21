package com.thustop_00.model;


public class Address{
    private String address ="";
    private double latitude = 0;
    private double longitude = 0;

    public static Address newInstance(String strAddress, double latitude, double longitude){
        Address address = new Address();
        address.address = strAddress;
        address.latitude = latitude;
        address.longitude = longitude;
        return address;
    }


    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
//public long dbTimestamp = 0;
}
