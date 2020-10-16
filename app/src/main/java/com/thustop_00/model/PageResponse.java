package com.thustop_00.model;

import java.util.List;

public class PageResponse<T> {
    public int count;
    public String next;
    public String previous;
    public List<T> results;
}
