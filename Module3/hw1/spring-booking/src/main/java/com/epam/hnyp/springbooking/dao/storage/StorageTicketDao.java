package com.epam.hnyp.springbooking.dao.storage;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.utils.BookingUtils;

public class StorageTicketDao implements TicketDao {

	private static final String TICKET_KEY_FORMAT = "ticket:{0}";
	private static final String TICKET_PREFIX = "ticket";
	
	private UserDao userDao;
	private EventDao eventDao;
	private Storage storage;
	
	@Override
	public Ticket create(Ticket ticket) {
		long id = BookingUtils.getNextToMaxLong(storage.getAllByPrefix(TICKET_PREFIX, Ticket.class), Ticket::getId);
		ticket.setId(id);
		return storage.put(getKey(id), ticket) ? ticket : null;
	}
	
	private String getKey(long ticketId) {
		return MessageFormat.format(TICKET_KEY_FORMAT, ticketId);
	}

	@Override
	public List<Ticket> getAllByUserIdSortedByEventDate(long userId, int pageSize, int pageNum) {
		Stream<Ticket> ticketStream = getTicketsStream().filter(t -> t.getUserId() == userId);
		List<Ticket> unsortedTickets = BookingUtils.getPageFromStream(ticketStream, pageSize, pageNum);
		return performSortByEventDateDescending(unsortedTickets);
	}
	
	private List<Ticket> performSortByEventDateDescending(List<Ticket> tickets) {
		List<Event> relatedEventsSortedByDate = tickets.stream()
			.map(t -> eventDao.getById(t.getEventId()))
			.sorted(getEventByDateComparator().reversed())
			.collect(Collectors.toList());
		
		Map<Long, List<Ticket>> ticketsGroupedByEvent = tickets.stream()
		        .collect(Collectors.groupingBy(Ticket::getEventId));
		
		return relatedEventsSortedByDate.stream()
		        .flatMap(e -> ticketsGroupedByEvent.get(e.getId()).stream())
		        .collect(Collectors.toList());
	}
	
	private Comparator<Event> getEventByDateComparator() {
	    return (e1, e2) -> BookingUtils.compareNullSafe(e1.getDate(), e2.getDate());
	}
	
	@Override
	public List<Ticket> getAllByEventIdSortedByUserEmail(long eventId, int pageSize, int pageNum) {
	    Stream<Ticket> ticketStream = getTicketsStream().filter(t -> t.getEventId() == eventId);
	    List<Ticket> unsortedTickets = BookingUtils.getPageFromStream(ticketStream, pageSize, pageNum);
	    return performSortByUserEmailAscending(unsortedTickets);
	}
	
	private List<Ticket> performSortByUserEmailAscending(List<Ticket> tickets) {
	    List<User> relatedUsersSortedByEmail = tickets.stream()
	            .map(t -> userDao.getById(t.getUserId()))
	            .sorted(getUserByEmailComparator().reversed())
	            .collect(Collectors.toList());
	    
	    Map<Long, List<Ticket>> ticketsGroupedByUser = tickets.stream()
	            .collect(Collectors.groupingBy(Ticket::getUserId));
	    
	    return relatedUsersSortedByEmail.stream()
	            .flatMap(u -> ticketsGroupedByUser.get(u.getId()).stream())
	            .collect(Collectors.toList());
	}
	
	private Comparator<User> getUserByEmailComparator() {
	    return (u1, u2) -> BookingUtils.compareNullSafe(u1.getEmail(), u2.getEmail());
	}

	@Override
	public boolean delete(long ticketId) {
		return storage.remove(getKey(ticketId));
	}
	
	private Stream<Ticket> getTicketsStream() {
		return storage.getAllByPrefix(TICKET_PREFIX, Ticket.class).stream();
	}

	@Override
	public Ticket getByEventIdAndPlace(long eventId, int place, Category category) {
		return getTicketsStream() 
				.filter(t -> t.getEventId() == eventId)
				.filter(t -> t.getPlace() == place)
				.filter(t -> t.getCategory() == category)
				.findFirst()
				.orElse(null);
	}

}
