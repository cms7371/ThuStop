package com.thustop_00.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Route {
    public String id;
    public int driver_id;
    public float distance;
    public int max_passenger;
    public int cnt_passenger;
    public String status;
    public int price;
    public List<Stop> stops;
}
