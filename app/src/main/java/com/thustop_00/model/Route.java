package com.thustop_00.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Route {
    public String id;
    public String region;
    public int driver_id;
    public float distance;
    public int max_passenger;
    public int cnt_passenger;
    public int capacity;
    public String status;
    public int price;
    public List<Stop> boarding_stops;
    public List<Stop> getting_off_stops;
}
