package com.epam.hnyp.springbooking.facade.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.model.impl.UserImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("/application-context.xml")
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingFacadeImplIntegrationTest {
	
	private static final String APPLICATION_CONTEXT = "application-context.xml";
	
//	@Autowired
    private BookingFacadeImpl bookingFacadeImpl;
    
    private ClassPathXmlApplicationContext applicationContext;
	
    @Before
	public void setUp() {
		applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT);
		bookingFacadeImpl = applicationContext.getBean("bookingFacadeImpl", BookingFacadeImpl.class);
	}
	
    @After
	public void cleanUp() {
    	bookingFacadeImpl = null;
		applicationContext.close();
	}
	
	@Test
    public void shouldCreateUser_whenPerformCreateUser() {
        User user = new UserImpl("my user", "user1@email");
        User createdUser = bookingFacadeImpl.createUser(user);
        assertThat(createdUser).isNotNull();
    }
    
    @Test
    public void shouldReturnNull_whenUserDoesNotExist() {
        User user = bookingFacadeImpl.getUserByEmail("user1@email");
        assertThat(user).isNull();
    }
    
    @Test
    public void shouldReturnUserByEmail_whenUserExist() {
        User user = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
        User storedUser = bookingFacadeImpl.getUserByEmail("user1@email");
        assertThat(storedUser).isEqualTo(user);
    }
    
    @Test
    public void shouldReturnAllUsersForName_whenPerformGetUsersByName() {
        User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
        User user2 = bookingFacadeImpl.createUser(new UserImpl("the user", "user2@email"));
        User user3 = bookingFacadeImpl.createUser(new UserImpl("antonio", "user3@email"));
        
        List<User> storedUsers = bookingFacadeImpl.getUsersByName("user", 10, 1);
        assertThat(storedUsers).contains(user1, user2);
    }
    
    @Test
    public void shouldCreateEvent_whenPerformCreateEvent() {
    	Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
    	assertThat(event1).isNotNull();
    }
    
    @Test
    public void shouldReturnAllEventsForTitle_whenPerformGetEventsByTitle() {
    	bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
    	bookingFacadeImpl.createEvent(new EventImpl("title title", new Date()));
    	bookingFacadeImpl.createEvent(new EventImpl("show must go on", new Date()));
    	
    	List<Event> storedEvents = bookingFacadeImpl.getEventsByTitle("title", 10, 1);
    	assertThat(storedEvents).hasSize(2);
    }
    
    @Test
    public void shouldCreateBookingForUserAndEvent_whenPerformBookTicketAndUserAndEventExist() {
    	User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
    	Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
    	
    	Ticket storedTicket = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 2, Ticket.Category.BAR);
    	assertThat(storedTicket).isNotNull();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformBookTicketAndUserDoesNotExist() {
    	Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
    	bookingFacadeImpl.bookTicket(0, event1.getId(), 2, Ticket.Category.BAR);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformBookTicketAndEventDoesNotExist() {
    	User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
    	bookingFacadeImpl.bookTicket(user1.getId(), 0, 2, Ticket.Category.BAR);
    }
    
    @Test
    public void shouldReturnAllBookedTicketsForUser_whenPerformGetBookedTickets() {
    	User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
    	User user2 = bookingFacadeImpl.createUser(new UserImpl("my user 2", "user2@email"));
    	Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
    	
    	Ticket ticket1 = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 2, Ticket.Category.BAR);
    	Ticket ticket2 = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 3, Ticket.Category.BAR);
    	Ticket ticket3 = bookingFacadeImpl.bookTicket(user2.getId(), event1.getId(), 4, Ticket.Category.BAR);
    	
    	List<Ticket> storedTickets = bookingFacadeImpl.getBookedTickets(user1, 10, 1);
    	assertThat(storedTickets).contains(ticket1, ticket2);
    }
    
    @Test
    public void shouldReturnAllBookedTicketsForEvent_whenPerformGetBookedTickets() {
        User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
        Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
        Event event2 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
        
        Ticket ticket1 = bookingFacadeImpl.bookTicket(user1.getId(), event2.getId(), 2, Ticket.Category.BAR);
        Ticket ticket2 = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 3, Ticket.Category.BAR);
        Ticket ticket3 = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 4, Ticket.Category.BAR);
        
        List<Ticket> storedTickets = bookingFacadeImpl.getBookedTickets(event1, 10, 1);
        assertThat(storedTickets).contains(ticket2, ticket3);
    }
    
    @Test
    public void shouldCancellTicket_whenPerformCancellTicket() {
        User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
        Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date()));
        
        Ticket ticket1 = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 2, Ticket.Category.BAR);
        boolean isCancelled = bookingFacadeImpl.cancelTicket(ticket1.getId());
        assertThat(isCancelled).isTrue(); 
    }
    
}
