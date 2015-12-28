package com.epam.hnyp.springbooking.facade.impl;

import static java.math.BigDecimal.valueOf;
import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.model.impl.UserImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-application-context.xml")
public class BookingFacadeImplIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static final BigDecimal ACCOUNT_AMOUNT = BigDecimal.TEN;
	private static final BigDecimal TICKET_PRICE = BigDecimal.TEN.subtract(valueOf(2.0D));
	
	@Autowired
    private BookingFacadeImpl bookingFacadeImpl;
	
	@Test
    public void shouldCreateUser_whenPerformCreateUser() {
        User user = new UserImpl("my user", "user1@email");
        User createdUser = bookingFacadeImpl.createUser(user);
        assertThat(createdUser).isNotNull();
    }
	
	@Test
    public void shouldCreateUserAccount_whenPerformCreateUser() {
		User user = new UserImpl("my user", "user1@email");
        User createdUser = bookingFacadeImpl.createUser(user);
        UserAccount account = bookingFacadeImpl.getUserAccount(createdUser.getId());
        assertThat(account).isNotNull();
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
    	
    	Ticket bookedTicket = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 2, Ticket.Category.BAR);
    	assertThat(bookedTicket).isNotNull();
    }
    
    @Test
    public void shouldWithdrawTicketPriceFromAccount_whenPerformBookTicket() {
    	User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
    	bookingFacadeImpl.refillUsersAccount(user1.getId(), ACCOUNT_AMOUNT);
    	
    	Event event1 = bookingFacadeImpl.createEvent(new EventImpl("title", new Date(), TICKET_PRICE));
    	
    	Ticket bookedTicket = bookingFacadeImpl.bookTicket(user1.getId(), event1.getId(), 2, Ticket.Category.BAR);
    	
    	UserAccount user1Account = bookingFacadeImpl.getUserAccount(user1.getId());
    	BigDecimal actualAccountAmount = user1Account.getPrepaidAmount();
    	
    	BigDecimal expectedAmountOnAccount = ACCOUNT_AMOUNT.subtract(TICKET_PRICE);
    	// 2 BigDecimal's could be not equal if precision differs, so using compareTo
    	assertThat(actualAccountAmount).isEqualByComparingTo(expectedAmountOnAccount);
    }
    
    @Test
    public void shouldRefillUserAccount_whenPerformRefillAccount() {
    	User user1 = bookingFacadeImpl.createUser(new UserImpl("my user", "user1@email"));
    	UserAccount user1Account = bookingFacadeImpl.getUserAccount(user1.getId());
    	BigDecimal initialAmountOnAccount = user1Account.getPrepaidAmount();

    	bookingFacadeImpl.refillUsersAccount(user1.getId(), ACCOUNT_AMOUNT);
    	
    	UserAccount user1AccountAfterRefill = bookingFacadeImpl.getUserAccount(user1.getId());
    	BigDecimal refilledAmountOnAccount = user1AccountAfterRefill.getPrepaidAmount();
    	
    	BigDecimal expectedAmountOnAccount = initialAmountOnAccount.add(ACCOUNT_AMOUNT);
    	assertThat(refilledAmountOnAccount).isEqualByComparingTo(expectedAmountOnAccount);
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
