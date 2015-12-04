package com.epam.hnyp.springbooking.service.impl;

import java.text.MessageFormat;
import java.util.List;

import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.service.TicketService;

public class TicketServiceImpl implements TicketService {

	private TicketDao ticketDao;
	
	@Override
	public Ticket bookTicket(User user, Event event, int place, Category category) {
		assertPlaceIsFree(event, place, category);
		
		Ticket ticket = createTicket();
		ticket.setCategory(category);
		ticket.setEventId(event.getId());
		ticket.setPlace(place);
		ticket.setUserId(user.getId());
		
		return ticketDao.create(ticket);
	}
	
	// who is responsible for creation ?  
	protected Ticket createTicket() {
		return new TicketImpl();
	}
	
	private void assertPlaceIsFree(Event event, int place, Category category) {
		Ticket ticket = ticketDao.getByEventIdAndPlace(event.getId(), place, category);
		if (ticket != null) {
			throw new IllegalStateException(MessageFormat.format("place {0} of category {1} for event {2} is already booked", place, category, event.getId()));
		}
	}

	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		long userId = user.getId();
		return ticketDao.getAllByUserIdSortedByEventDate(userId, pageSize, pageNum);
	}

	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		long eventId = event.getId();
		return ticketDao.getAllByEventIdSortedByUserEmail(eventId, pageSize, pageNum);
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return ticketDao.delete(ticketId);
	}
	
}
