package com.epam.hnyp.springbooking.dao;

import java.util.Date;
import java.util.List;

import com.epam.hnyp.springbooking.model.Event;

public interface EventDao {
	
	Event getById(long eventId);

	List<Event> getAllByTitle(String title, int pageSize, int pageNum);

	List<Event> getAllForDay(Date day, int pageSize, int pageNum);

	Event create(Event event);

	Event update(Event event);

	boolean delete(long eventId);
	
}
