package com.thustop.thestop.model;


import java.util.List;

public class Ticket {
    public int ticketID;
    public int userID;
    public int routeID;
    public int boarding_via_position;
    public int alighting_via_position;
    public String status;
    public String end_date;
    public List<String> unavailable_dates;
    public Route route;
}
