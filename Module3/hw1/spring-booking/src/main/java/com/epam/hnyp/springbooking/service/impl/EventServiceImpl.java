package com.epam.hnyp.springbooking.service.impl;

import java.util.Date;
import java.util.List;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.service.EventService;

public class EventServiceImpl implements EventService {

	private EventDao eventDao;
	
	@Override
	public Event getEventById(long eventId) {
		return eventDao.getById(eventId);
	}

	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventDao.getAllByTitle(title, pageSize, pageNum);
	}

	@Override
	public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
		return eventDao.getAllForDay(day, pageSize, pageNum);
	}

	@Override
	public Event createEvent(Event event) {
		return eventDao.create(event);
	}

	@Override
	public Event updateEvent(Event event) {
		return eventDao.update(event);
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return eventDao.delete(eventId);
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

}
