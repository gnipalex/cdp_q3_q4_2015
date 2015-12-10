package com.epam.hnyp.springbooking.model.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Objects;

import com.epam.hnyp.springbooking.model.Event;

public class EventImpl implements Event {

    private long id;
    private String title;
    private Date date;
    
    public EventImpl() {
	}
    
    public EventImpl(String title, Date date) {
		this.title = title;
		this.date = date;
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
	public String getTitle() {
        return title;
    }

    @Override
	public void setTitle(String title) {
        this.title = title;
    }

    @Override
	public Date getDate() {
        return date;
    }

    @Override
	public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
    	return MessageFormat.format("Event[id={0}; title={1}; date={2}]", id, title, date);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(id, title, date);
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null || !(obj instanceof Event)) {
    		return false;
    	}
    	Event other = (Event)obj;
    	return this == other || Objects.equals(id, other.getId())
    	        && Objects.equals(title, other.getTitle())
    	        && Objects.equals(date, other.getDate());

    }

}
