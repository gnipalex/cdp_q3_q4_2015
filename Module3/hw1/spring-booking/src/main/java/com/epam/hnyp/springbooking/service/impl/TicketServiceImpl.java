package com.epam.hnyp.springbooking.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;

import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.service.TicketService;
import com.epam.hnyp.springbooking.service.UserAccountService;

public class TicketServiceImpl implements TicketService {

    private static final Logger LOG = Logger.getLogger(TicketServiceImpl.class);
    
    @Autowired
	private TicketDao ticketDao;
	@Autowired
    private UserAccountService userAccountService;
	
	@Override
	public Ticket bookTicket(User user, Event event, int place, Category category) {
		assertPlaceIsFree(event, place, category);
		
        BigDecimal ticketPrice = event.getTicketPrice();
        UserAccount account = userAccountService.getAccountByUserId(user.getId());
        userAccountService.withdrawAmountFromAccount(account, ticketPrice);
        LOG.info(MessageFormat.format("amount {0} to withdraw from account {1}", ticketPrice, account.getUserId()));
		
        Ticket ticket = createTicket(user, event, place, category);
		return ticketDao.create(ticket);
	}

    private Ticket createTicket(User user, Event event, int place, Category category) {
        Ticket ticket = createTicketInstance();
		ticket.setCategory(category);
		ticket.setEventId(event.getId());
		ticket.setPlace(place);
		ticket.setUserId(user.getId());
        return ticket;
    }
	
    @Lookup("ticketInstance")
	protected Ticket createTicketInstance() {
		throw new UnsupportedOperationException();
	}
	
	private void assertPlaceIsFree(Event event, int place, Category category) {
		Ticket ticket = ticketDao.getByIdAndPlaceAndCategory(event.getId(), place, category);
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
