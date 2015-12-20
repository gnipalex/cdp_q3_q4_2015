package com.epam.hnyp.springbooking.facade.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.service.EventService;
import com.epam.hnyp.springbooking.service.TicketService;
import com.epam.hnyp.springbooking.service.UserAccountService;
import com.epam.hnyp.springbooking.service.UserService;

public class BookingFacadeImpl implements BookingFacade {

    private static final Logger LOG = Logger.getLogger(BookingFacadeImpl.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserAccountService userAccountService;

    @Override
    public Event getEventById(long eventId) {
        Event event = eventService.getEventById(eventId);
        logEvent(eventId, event);
        return event;
    }

    private void logEvent(long id, Event foundEvent) {
        if (foundEvent == null) {
            LOG.info(MessageFormat.format("event with id {0} not found", id));
        } else {
            LOG.info(MessageFormat.format("found event {0}", foundEvent));
        }
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Event> events = eventService.getEventsByTitle(title, pageSize, pageNum);
        LOG.info(MessageFormat.format("found {0} events for title {1}, page = {2}, pagesize = {3}",
                events.size(), title, pageNum, pageSize));
        return events;
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        List<Event> events = eventService.getEventsForDay(day, pageSize,
                pageNum);
        LOG.info(MessageFormat.format("found {0} events for day {1}, page = {2}, pagesize = {3}",
                events.size(), day, pageNum, pageSize));
        return events;
    }

    @Override
    public Event createEvent(Event event) {
        Event createdEvent = eventService.createEvent(event);
        if (createdEvent == null) {
            LOG.info(MessageFormat.format("event was not created, {0}", event));
        }
        return createdEvent;
    }

    @Override
    public Event updateEvent(Event event) {
        Event updatedEvent = eventService.updateEvent(event);
        if (updatedEvent == null) {
            LOG.info(MessageFormat.format("event was not updated, {0}", event));
        }
        return updatedEvent;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        boolean isDeleted = eventService.deleteEvent(eventId);
        if (!isDeleted) {
            LOG.info(MessageFormat.format("event with id {0} was not deleted", eventId));
        }
        return isDeleted;
    }

    @Override
    public User getUserById(long userId) {
        User user = userService.getUserById(userId);
        logUser(userId, user);
        return user;
    }

    private void logUser(long userId, User foundUser) {
        if (foundUser == null) {
            LOG.info(MessageFormat.format("user with id {0} not found", userId));
        } else {
            LOG.info(MessageFormat.format("found user {0}", foundUser));
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User userByEmail = userService.getUserByEmail(email);
        if (userByEmail == null) {
            LOG.info(MessageFormat.format("user for email {0} not found", email));
        }
        return userByEmail;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            LOG.error(MessageFormat.format("user was not created, {0}", user));
            return null;
        }
        UserAccount account = userAccountService.createAccount(createdUser);
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser == null) {
            LOG.error(MessageFormat.format("user was not updated, {0}", user));
        }
        return updatedUser;
    }

    @Override
    public boolean deleteUser(long userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (!isDeleted) {
            LOG.info(MessageFormat.format("user by id {0} was not removed", userId));
        }
        return isDeleted;
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place,
            Category category) {
        // need to check whether user/event exist
        User user = userService.getUserById(userId);
        assertNotNull(user, MessageFormat.format("user with id {0} does not exist", userId));
        UserAccount account = userAccountService.getAccountByUserId(userId);
        assertNotNull(account, MessageFormat.format("account for user {0} does not exist", userId));
        Event event = eventService.getEventById(eventId);
        assertNotNull(event, MessageFormat.format("event with id {0} does not exist", eventId));
        
        BigDecimal ticketPrice = event.getTicketPrice();
        userAccountService.withdrawAmountFromAccount(account, ticketPrice);

        return ticketService.bookTicket(user, event, place, category);
    }

    private void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketService.getBookedTickets(user, pageSize, pageNum);
        LOG.info(MessageFormat.format("found {0} tickets for user {1}, page = {2}, pagesize = {3}", 
        		tickets.size(), user, pageNum, pageSize));
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketService.getBookedTickets(event, pageSize, pageNum);
        LOG.info(MessageFormat.format("found {0} tickets for event {1}, page = {2}, pagesize = {3}",
                tickets.size(), event, pageNum, pageSize));
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        boolean isCancelled = ticketService.cancelTicket(ticketId);
        if (!isCancelled) {
            LOG.info(MessageFormat.format("ticket with id {0} is not calcelled", ticketId));
        }
        return isCancelled;
    }

    @Override
    public UserAccount getUserAccount(long userId) {
        return userAccountService.getAccountByUserId(userId);
    }

    @Override
    public boolean refillUsersAccount(long userId, BigDecimal amount) {
        UserAccount account = userAccountService.getAccountByUserId(userId);
        if (account == null) {
            LOG.error(MessageFormat.format("account {0} doesn't exist", userId));
            return false;
        }
        try {
            userAccountService.refillAccountWithAmount(account, amount);
            LOG.info(MessageFormat.format("account {0} was refilled", userId));
            return true;
        } catch (IllegalArgumentException e) {
            LOG.error(MessageFormat.format("can not refill account {0}", userId), e);
            return false;
        }
    }

}
