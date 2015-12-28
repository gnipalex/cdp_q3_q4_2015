package com.epam.hnyp.springbooking.dao;

import java.util.List;

import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;;

public interface TicketDao {
    
	Ticket create(Ticket ticket);

    List<Ticket> getAllByUserIdSortedByEventDate(long userId, int pageSize, int pageNum);

    List<Ticket> getAllByEventIdSortedByUserEmail(long eventId, int pageSize, int pageNum);
    
    Ticket getByIdAndPlaceAndCategory(long eventId, int place, Category category);
    
    boolean delete(long ticketId);
    
}
