package com.thustop_00.model;

public class Stop {
    public String name;
    public int id;
    public String address;
    public float latitude;
    public float longitude;


    public Stop(String name, int id, String address, float latitude, float longitude) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
