package com.thustop_00.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Route {
    private static final String TAG = "modelRoute";
    public int id;
    public String name;
//  public String region;
    public Via start;
    public Via end;
    public float distance;
    public Bus bus;
    public String status;
    public int max_passenger;
    public int cnt_passenger;
    public int default_price;
    public int discount_price;
    public List<Via> vias;
    public ArrayList<Via> boarding_stops;
    public ArrayList<Via> alighting_stops;

/*    public Route(String id, String region, int driver_id, float distance, int max_passenger, int cnt_passenger, int spec, String status, int price) {
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
    }*/
    public void initialize(){
        boarding_stops = new ArrayList<Via>();
        alighting_stops = new ArrayList<Via>();
        for(Via v : vias){
            if (v.type.equals("승차 가능")){
                boarding_stops.add(v);
            } else if (v.type.equals("하차 가능")){
                alighting_stops.add(v);
            } else{
                Log.e(TAG, "initialize: 정의 되지 않은 Via type" + v.type);
            }
        }
    }

    public String getBoardingStopName(int index){
        return this.boarding_stops.get(index).stop.name;
    }
    public String getBoardingStopTime(int index){
        return this.boarding_stops.get(index).time;
    }
    public String getAlightingStopName(int index){
        return this.alighting_stops.get(index).stop.name;
    }
    public String getAlightingStopTime(int index){
        return this.alighting_stops.get(index).time;
    }

}
