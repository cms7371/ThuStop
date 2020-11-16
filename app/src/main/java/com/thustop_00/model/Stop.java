package com.thustop_00.model;

public class Stop {
    public String name;
    public int id;
    public float latitude;
    public float longitude;

    public Stop(String name, int id, float latitude, float longitude) {
        this.name = name;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
