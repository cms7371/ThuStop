package com.thustop_00.model;

import org.parceler.Parcel;

@Parcel
public class Route {
    public int id;
    public String name;
    public double distance;
    public int max_passenger;
    public int cnt_passenger;
    public String status;
    public int default_price;
    public int discount_price;
    public String my_status;
}
