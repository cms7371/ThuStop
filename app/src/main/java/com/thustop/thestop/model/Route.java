package com.thustop.thestop.model;

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
    //  public String start_date;
    public int max_passenger;
    public int cnt_passenger;
    public int default_price;
    public int discount_price;
    public List<Via> vias;
    public ArrayList<Via> boarding_stops;
    public ArrayList<Via> alighting_stops;
    public double minimum_distance = 10000000;

    public void initialize() {
        boarding_stops = new ArrayList<Via>();
        alighting_stops = new ArrayList<Via>();
        for (Via v : vias) {
            if (v.type.equals("승차 가능")) {
                boarding_stops.add(v);
            } else if (v.type.equals("하차 가능")) {
                alighting_stops.add(v);
            } else {
                Log.e(TAG, "initialize: 정의 되지 않은 Via type" + v.type);
            }
        }
    }

    public void updateMinimumDistance(double latitude, double longitude) {
        double current_distance;
        for (Via v : vias) {
            try {
                current_distance = Math.sqrt(Math.pow(v.stop.pos.latitude - latitude, 2) + Math.pow(v.stop.pos.longitude - longitude, 2));
                Log.d(TAG, "updateMinimumDistance: 정류장 " + v.stop.name + " 거리 " + current_distance);
                if (current_distance < minimum_distance)
                    minimum_distance = current_distance;
            } catch (NullPointerException e) {
                Log.e(TAG, "updateMinimumDistance: 노선 거리 측정 에러," + " 에러 정류장 " + v.stop.name, e);
            }


        }
    }

    public double getMinimumDistance() {
        Log.d(TAG, "getMinimumDistance: 노선" + name + " 최소 거리 " + minimum_distance);
        return minimum_distance;
    }

    public String getBoardingStopName(int index) {
        return this.boarding_stops.get(index).stop.name;
    }

    public String getBoardingStopTime(int index) {
        return this.boarding_stops.get(index).time;
    }

    public String getAlightingStopName(int index) {
        return this.alighting_stops.get(index).stop.name;
    }

    public String getAlightingStopTime(int index) {
        return this.alighting_stops.get(index).time;
    }

}
