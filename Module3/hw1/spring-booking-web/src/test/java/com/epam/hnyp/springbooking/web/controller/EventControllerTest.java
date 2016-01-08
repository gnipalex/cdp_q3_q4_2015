package com.epam.hnyp.springbooking.web.controller;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest {

    private static final long EVENT_ID = 1;
    private static final String TITLE = "title";
    private static final int PAGE_NUMBER = 2;
    private static final int PAGE_SIZE = 3;
    private static final Date DAY = new Date();
    
    @Mock
    private EventImpl mockEvent;
    @Mock
    private TicketImpl mockTicket; 
    @Mock
    private BookingFacade mockBookingFacade;
    @Mock
    private HttpServletResponse mockHttpResponse;
    @InjectMocks
    private EventController eventController;
    
    @Test
    public void shouldCallBookingFacadeCreateEvent_whenPerformCreateEvent() {
        eventController.createEvent(mockEvent);
        verify(mockBookingFacade).createEvent(mockEvent);
    }
    
    @Test
    public void shouldReturnCreatedEvent_whenPerformCreateEvent() {
        when(mockBookingFacade.createEvent(mockEvent)).thenReturn(mockEvent);
        Event result = eventController.createEvent(mockEvent);
        assertThat(result).isEqualTo(mockEvent);
    }
    
    @Test
    public void shouldCallBookingFacadeGetEventById_whenPerformGetEventById() {
        eventController.getEventById(EVENT_ID);
        verify(mockBookingFacade).getEventById(EVENT_ID);
    }
    
    @Test
    public void shouldReturnEvent_whenPerformGetEventById() {
        when(mockBookingFacade.getEventById(EVENT_ID)).thenReturn(mockEvent);
        Event result = eventController.getEventById(EVENT_ID);
        assertThat(result).isEqualTo(mockEvent);
    }
    
    @Test
    public void shouldCallBookingFacadeUpdateEvent_whenPerformUpdateEvent() {
        eventController.updateEvent(EVENT_ID, mockEvent);
        verify(mockBookingFacade).updateEvent(mockEvent);
    }
    
    @Test
    public void shouldSetIdToModelBeingUpdated_whenPerformUpdateEvent() {
        eventController.updateEvent(EVENT_ID, mockEvent);
        verify(mockEvent).setId(EVENT_ID);
    }
    
    @Test
    public void shouldReturnUpdatedEvent_whenPerformUpdateEvent() {
        when(mockBookingFacade.updateEvent(mockEvent)).thenReturn(mockEvent);
        Event result = eventController.updateEvent(EVENT_ID, mockEvent);
        assertThat(result).isEqualTo(mockEvent);
    }
    
    @Test
    public void shouldCallBookingFacadeDeleteEvent_whenPerformDeleteEvent() throws IOException {
        eventController.deleteEvent(EVENT_ID, mockHttpResponse);
        verify(mockBookingFacade).deleteEvent(EVENT_ID);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformDeleteEventAndEventWasNotDeleted() throws IOException {
        when(mockBookingFacade.deleteEvent(EVENT_ID)).thenReturn(false);
        eventController.deleteEvent(EVENT_ID, mockHttpResponse);
        verify(mockHttpResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }
    
    @Test
    public void shouldCallBookingFacadeGetEventsByTitle_whenPerformGetEventsByTitle() {
        eventController.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUMBER);
        verify(mockBookingFacade).getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldReturnEvents_whenPerformGetEventsByTitle() {
        when(mockBookingFacade.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUMBER))
            .thenReturn(asList(mockEvent));
        List<Event> results = eventController.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUMBER);
        assertThat(results).containsOnly(mockEvent);
    }
    
    @Test
    public void shouldCallBookingFacadeGetEventsForDay_whenPerformGetEventsForDay() {
        eventController.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUMBER);
        verify(mockBookingFacade).getEventsForDay(DAY, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldReturnEvents_whenPerformGetEventsForDay() {
        when(mockBookingFacade.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUMBER))
            .thenReturn(asList(mockEvent));
        List<Event> results = eventController.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUMBER);
        assertThat(results).containsOnly(mockEvent);
    }
    
    @Test
    public void shouldCallBookingFacadeGetBookedTickets_whenPerformGetTicketsForEvent() throws IOException {
        when(mockBookingFacade.getEventById(EVENT_ID)).thenReturn(mockEvent);
        eventController.getTicketsForEvent(EVENT_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpResponse);
        verify(mockBookingFacade).getBookedTickets(mockEvent, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformGetTicketsForEvent() throws IOException {
        when(mockBookingFacade.getEventById(EVENT_ID)).thenReturn(null);
        eventController.getTicketsForEvent(EVENT_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpResponse);
        verify(mockHttpResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }
    
    @Test
    public void shouldReturnTickets_whenPerformGetTicketsForEvent() throws IOException {
        when(mockBookingFacade.getEventById(EVENT_ID)).thenReturn(mockEvent);
        when(mockBookingFacade.getBookedTickets(mockEvent, PAGE_SIZE, PAGE_NUMBER))
            .thenReturn(asList(mockTicket));
        List<Ticket> tickets = eventController.getTicketsForEvent(EVENT_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpResponse);
        assertThat(tickets).containsOnly(mockTicket);
    }
    
}
