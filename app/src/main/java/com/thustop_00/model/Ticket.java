package com.thustop_00.model;


public class Ticket {
    public int ticketID;
    public int userID;
    public Route route;
    public int boarding_via_position;
    public int alighting_via_position;
    public String status;
    public String end_date; //TODO : 포맷 고려
    public String QR_key;
}
