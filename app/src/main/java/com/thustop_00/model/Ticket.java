package com.thustop_00.model;

import java.util.ArrayList;

public class Ticket {
    String ticketID;
    String userID;
    String routeID;
    String status;
    int boarding_stop;
    int alighting_stop;
    String date; //TODO : 포맷 고려

    public Ticket(String routeID, String status, int boarding_stop, int alighting_stop, String date) {
        this.routeID = routeID;
        this.status = status;
        this.boarding_stop = boarding_stop;
        this.alighting_stop = alighting_stop;
        this.date = date;
    }
}
