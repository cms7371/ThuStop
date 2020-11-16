package com.thustop_00.model;

import java.util.ArrayList;


public class Route {
    public String id;
    public String region;
    public int driver_id;
    public float distance;
    public int max_passenger;
    public int cnt_passenger;
    public int spec;
    public String status;
    public int price;
    public ArrayList<Via> boarding_stops;
    public ArrayList<Via> alighting_stops;

    public Route(String id, String region, int driver_id, float distance, int max_passenger, int cnt_passenger, int spec, String status, int price) {
        this.id = id;
        this.region = region;
        this.driver_id = driver_id;
        this.distance = distance;
        this.max_passenger = max_passenger;
        this.cnt_passenger = cnt_passenger;
        this.spec = spec;
        this.status = status;
        this.price = price;
        this.boarding_stops = new ArrayList<Via>();
        this.alighting_stops = new ArrayList<Via>();
    }

    public String getBoardingStopName(int index){
        return this.boarding_stops.get(index).stop.name;
    }
    public String getAlightingStopName(int index){
        return this.alighting_stops.get(index).stop.name;
    }

}
