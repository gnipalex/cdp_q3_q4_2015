package com.epam.hnyp.springbooking.facade.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.service.EventService;
import com.epam.hnyp.springbooking.service.TicketService;
import com.epam.hnyp.springbooking.service.UserService;

public class BookingFacadeImpl implements BookingFacade {

	private EventService eventService;
	private UserService userService;
	private TicketService ticketService;
	
	@Override
	public Event getEventById(long eventId) {
		return eventService.getEventById(eventId);
	}

	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventService.getEventsByTitle(title, pageSize, pageNum);
	}

	@Override
	public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
		return eventService.getEventsForDay(day, pageSize, pageNum);
	}

	@Override
	public Event createEvent(Event event) {
		return eventService.createEvent(event);
	}

	@Override
	public Event updateEvent(Event event) {
		return eventService.updateEvent(event);
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return eventService.deleteEvent(eventId);
	}

	@Override
	public User getUserById(long userId) {
		return userService.getUserById(userId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userService.getUserByEmail(email);
	}

	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return userService.getUsersByName(name, pageSize, pageNum);
	}

	@Override
	public User createUser(User user) {
		return userService.createUser(user);
	}

	@Override
	public User updateUser(User user) {
		return userService.updateUser(user);
	}

	@Override
	public boolean deleteUser(long userId) {
		return userService.deleteUser(userId);
	}

	@Override
	public Ticket bookTicket(long userId, long eventId, int place, Category category) {
		// need to check whether user/event exist
		User user = userService.getUserById(userId);
		Event event = eventService.getEventById(eventId);
		assertNotNull(user, MessageFormat.format("user with id {0} does not exist", userId));
		assertNotNull(event, MessageFormat.format("event with id {0} does not exist", eventId));
		
		return ticketService.bookTicket(user, event, place, category);
	}
	
	private void assertNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(user, pageSize, pageNum);
	}

	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(event, pageSize, pageNum);
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return ticketService.cancelTicket(ticketId);
	}

}
