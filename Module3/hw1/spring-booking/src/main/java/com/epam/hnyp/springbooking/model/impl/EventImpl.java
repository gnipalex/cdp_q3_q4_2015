package com.epam.hnyp.springbooking.model.impl;

import java.util.Date;

import com.epam.hnyp.springbooking.model.Event;

public class EventImpl implements Event {

    private long id;
    private String title;
    private Date date;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
