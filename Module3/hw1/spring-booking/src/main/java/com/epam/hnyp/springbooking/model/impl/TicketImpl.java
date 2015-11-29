package com.epam.hnyp.springbooking.model.impl;

import com.epam.hnyp.springbooking.model.Ticket;

public class TicketImpl implements Ticket {

    private long id;
    private long eventId;
    private long userId;
    private Category category;
    private int place;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;        
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

}
