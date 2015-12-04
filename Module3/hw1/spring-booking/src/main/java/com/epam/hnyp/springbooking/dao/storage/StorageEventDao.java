package com.epam.hnyp.springbooking.dao.storage;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.utils.BookingUtils;

public class StorageEventDao implements EventDao {

	private static final String EVENT_KEY_FORMAT = "event:{0}";
	private static final String EVENT_PREFIX = "event";
	
	private Storage storage;
	
	@Override
	public Event getById(long eventId){
		return storage.get(getKey(eventId), Event.class);
	}

	private String getKey(long eventId) {
		return MessageFormat.format(EVENT_KEY_FORMAT, eventId);
	}

	@Override
	public List<Event> getAllByTitle(String title, int pageSize, int pageNum) {
		Stream<Event> eventStream = storage.getAllByPrefix(EVENT_PREFIX, Event.class)
				.stream().filter(e -> StringUtils.contains(e.getTitle(), title));
		return BookingUtils.getPageFromStream(eventStream, pageSize, pageNum);
	}
	
	@Override
	public List<Event> getAllForDay(Date day, int pageSize, int pageNum) {
		Stream<Event> eventStream = storage.getAllByPrefix(EVENT_PREFIX, Event.class)
				.stream().filter(e -> day.equals(e.getDate()));
		return BookingUtils.getPageFromStream(eventStream, pageSize, pageNum);
	}

	@Override
	public Event create(Event event) {
		long id = BookingUtils.getNextToMaxLong(storage.getAllByPrefix(EVENT_PREFIX, Event.class), Event::getId);
		event.setId(id);
		return storage.put(getKey(id), event) ? event : null;
	}

	@Override
	public Event update(Event event) {
		String key = getKey(event.getId());
		Event updatedEvent = null;
		if (storage.remove(key)) {
			storage.put(key, event);
			updatedEvent = event;
		}
		return updatedEvent;
	}

	@Override
	public boolean delete(long eventId) {
		return storage.remove(getKey(eventId));
	}

}
