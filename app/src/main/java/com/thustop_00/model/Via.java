package com.thustop_00.model;

public class Via {
    public int id;
    public Stop stop;
    public String time;

    public Via(int id, Stop stop, String time) {
        this.id = id;
        this.stop = stop;
        this.time = time;
    }
}
