package com.epam.hnyp.springbooking.dao.storage;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;

@RunWith(MockitoJUnitRunner.class)
public class StorageTicketDaoTest {

	private static final String TICKET_KEY_FORMAT = "ticket:{0}";
	private static final String TICKET_PREFIX = "ticket";
	
	private static final long ID_1 = 1L;
	private static final int PLACE_1 = 5;
	
	private static final long EVENT1_ID = 21L;
	private static final long EVENT2_ID = 22L;
	private static final long USER1_ID = 11L;
	private static final long USER2_ID = 12L;
	
	private static final Date DATE_1 = DateUtils.addDays(new Date(), -1);
	private static final Date DATE_2 = DateUtils.addDays(new Date(), 1);

	@Mock
	private User mockUser1;
	@Mock
	private User mockUser2;
	@Mock
	private Event mockEvent1;
	@Mock
	private Event mockEvent2;
	
	@Mock
	private Ticket mockTicket1;
	@Mock
	private Ticket mockTicket2;
	@Mock
	private Ticket mockTicket3;
	
	@Mock
	private UserDao mockUserDao;
	@Mock
	private EventDao mockEventDao;
	@Mock
	private Storage mockStorage;
	
	@InjectMocks
	private StorageTicketDao storageTicketDao;
	
	@Before
	public void setUp() {
		when(mockUserDao.getById(USER1_ID)).thenReturn(mockUser1);
		when(mockUserDao.getById(USER2_ID)).thenReturn(mockUser2);
		when(mockEventDao.getById(EVENT1_ID)).thenReturn(mockEvent1);
		when(mockEventDao.getById(EVENT2_ID)).thenReturn(mockEvent2);

		when(mockUser1.getEmail()).thenReturn("email_1");
		when(mockUser2.getEmail()).thenReturn("email_2");
		when(mockUser1.getId()).thenReturn(USER1_ID);
		when(mockUser2.getId()).thenReturn(USER2_ID);
		
		when(mockEvent1.getDate()).thenReturn(DATE_1);
		when(mockEvent2.getDate()).thenReturn(DATE_2);
		when(mockEvent1.getId()).thenReturn(EVENT1_ID);
		when(mockEvent2.getId()).thenReturn(EVENT2_ID);
		
		// belongs to event1 and user1
		when(mockTicket1.getId()).thenReturn(ID_1);
		when(mockTicket1.getEventId()).thenReturn(EVENT1_ID);
		when(mockTicket1.getUserId()).thenReturn(USER1_ID);
		//belongs to event2 and user1
		when(mockTicket2.getId()).thenReturn(2L);
		when(mockTicket2.getEventId()).thenReturn(EVENT2_ID);
		when(mockTicket2.getUserId()).thenReturn(USER1_ID);
		//belongs to event2 and user2
		when(mockTicket3.getId()).thenReturn(3L);
		when(mockTicket3.getEventId()).thenReturn(EVENT2_ID);
		when(mockTicket3.getUserId()).thenReturn(USER2_ID);

		when(mockStorage.getAllByPrefix(TICKET_PREFIX, Ticket.class))
			.thenReturn(asList(mockTicket1, mockTicket2, mockTicket3));
	}
	
	private String formatKey(long id) {
		return MessageFormat.format(TICKET_KEY_FORMAT, id);
	}
	
	@Test
	public void shouldReturnTicket_whenPerformGetByEventIdAndPlaceAndTicketExists() {
		when(mockTicket1.getPlace()).thenReturn(PLACE_1);
		when(mockTicket1.getCategory()).thenReturn(Category.BAR);
		Ticket storedTicket = storageTicketDao.getByEventIdAndPlace(EVENT1_ID, PLACE_1, Category.BAR);
		assertThat(storedTicket).isNotNull();
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetByEventIdAndPlaceAndTicketDoesNotExist() {
		when(mockTicket1.getPlace()).thenReturn(PLACE_1);
		when(mockTicket1.getCategory()).thenReturn(Category.PREMIUM);
		Ticket storedTicket = storageTicketDao.getByEventIdAndPlace(EVENT1_ID, PLACE_1, Category.BAR);
		assertThat(storedTicket).isNull();
	}
	
	@Test
	public void shouldReturnTicketsSortedByEventDate_whenPerformGetAllByUserIdSortedByEventDate() {
		//expect to get here ticket2 and ticket1
		List<Ticket> sortedTickets = storageTicketDao.getAllByUserIdSortedByEventDate(USER1_ID, 10, 1);
		//ticket2 event date is greater than ticket1 event date
		assertThat(sortedTickets).containsExactly(mockTicket2, mockTicket1);
	}

	@Test
	public void shouldReturnEmptyList_whenPerformGetAllByUserIdSortedByEventDateAndTicketsAreNotFound() {
		when(mockStorage.getAllByPrefix(TICKET_PREFIX, Ticket.class)).thenReturn(emptyList());
		List<Ticket> sortedTickets = storageTicketDao.getAllByUserIdSortedByEventDate(USER1_ID, 10, 1);
		assertThat(sortedTickets).isEmpty();
	}
	
	@Test
	public void shouldReturnTicketsSortedByUserEmail_whenPerformGetAllByEventIdSortedByUserEmail() {
		// expect to get here ticket2 and ticket3
		List<Ticket> sortedTickets = storageTicketDao.getAllByEventIdSortedByUserEmail(EVENT2_ID, 10, 1);
		// ticket2 users email is lower than ticket3 users email
		assertThat(sortedTickets).containsExactly(mockTicket2, mockTicket3);
	}
	
	@Test
	public void shouldReturnEmptyList_whenPerformGetAllByEventIdSortedByUserEmailAndTicketsAreNotFound() {
		when(mockStorage.getAllByPrefix(TICKET_PREFIX, Ticket.class)).thenReturn(emptyList());
		List<Ticket> sortedTickets = storageTicketDao.getAllByUserIdSortedByEventDate(USER1_ID, 10, 1);
		assertThat(sortedTickets).isEmpty();
	}
	
	@Test
	public void shouldSaveUserWithGeneratedID_whenPerformCreate() {
		Ticket newTicket = mock(Ticket.class);
		storageTicketDao.create(newTicket);
		
		long expectedGeneratedID = 4L;
		verify(newTicket).setId(expectedGeneratedID);
		verify(mockStorage).put(formatKey(expectedGeneratedID), newTicket);
	}
	
	@Test
	public void shouldRemoveEvent_whenPerformDelete() {
		storageTicketDao.delete(ID_1);
		String key = formatKey(ID_1);
		verify(mockStorage).remove(key);
	}

}
