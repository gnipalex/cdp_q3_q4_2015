package com.epam.hnyp.springbooking.model.impl;

import java.text.MessageFormat;
import java.util.Objects;

import com.epam.hnyp.springbooking.model.Ticket;

public class TicketImpl implements Ticket {

    private long id;
    private long eventId;
    private long userId;
    private Category category;
    private int place;
    
    public TicketImpl() {
	}
    
    public TicketImpl(long eventId, long userId, Category category, int place) {
		this.eventId = eventId;
		this.userId = userId;
		this.category = category;
		this.place = place;
	}

	@Override
	public long getId() {
        return id;
    }

    @Override
	public void setId(long id) {
        this.id = id;        
    }

    @Override
	public long getEventId() {
        return eventId;
    }

    @Override
	public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
	public long getUserId() {
        return userId;
    }

    @Override
	public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
	public Category getCategory() {
        return category;
    }

    @Override
	public void setCategory(Category category) {
        this.category = category;
    }

    @Override
	public int getPlace() {
        return place;
    }

    @Override
	public void setPlace(int place) {
        this.place = place;
    }
    
    @Override
    public String toString() {
    	return MessageFormat.format("Ticket[id={0}; eventId={1}; userId={2}; category={3}; place={4}]", 
    			id, eventId, userId, category, place);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userId, eventId, category, place);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) obj;
        return this == other || Objects.equals(id, other.getId())
                && Objects.equals(eventId, other.getEventId())
                && Objects.equals(userId, other.getUserId())
                && Objects.equals(category, other.getCategory())
                && Objects.equals(place, other.getPlace());
    }

}
