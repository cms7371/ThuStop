package com.thustop.thestop.model;


import java.util.List;

public class Ticket {
    public Ticket (int routeID, int boarding_via_id, int alighting_via_id, String start_date, String end_date){
        this.route = routeID;
        this.start_via = boarding_via_id;
        this.end_via = alighting_via_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    public int id;
    public int userID;
    public int route;
    public int start_via;
    public int end_via;
    public String status;
    public String start_date;
    public String end_date;
    public List<RefundDate> refund_dates;
}
