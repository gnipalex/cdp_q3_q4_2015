package com.epam.hnyp.springbooking.web.controller;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;

@RunWith(MockitoJUnitRunner.class)
public class TicketControllerTest {

    private static final long TICKET_ID = 7;
    private static final long USER_ID = 1;
    private static final long EVENT_ID = 2;
    private static final int PLACE = 3;
    private static final Ticket.Category CATEGORY = Category.BAR;
    
    @Mock
    private BookingFacade mockBookingFacade;
    @Mock
    private TicketImpl mockTicket;
    @Mock
    private HttpServletResponse mockHttpServletResponse;
    
    @InjectMocks
    private TicketController ticketController;
    
    @Before
    public void setUp() {
        when(mockTicket.getUserId()).thenReturn(USER_ID);
        when(mockTicket.getEventId()).thenReturn(EVENT_ID);
        when(mockTicket.getPlace()).thenReturn(PLACE);
        when(mockTicket.getCategory()).thenReturn(CATEGORY);
    }
    
    @Test
    public void shouldCallBookingFacadeBookTicket_whenPerformBookTicket() {
        ticketController.bookTicket(mockTicket);
        verify(mockBookingFacade).bookTicket(USER_ID, EVENT_ID, PLACE, CATEGORY);
    }
    
    @Test
    public void shouldReturnCreatedTicket_whenPerformBookTicket() {
        when(mockBookingFacade.bookTicket(USER_ID, EVENT_ID, PLACE, CATEGORY)).thenReturn(mockTicket);
        Ticket result = ticketController.bookTicket(mockTicket);
        assertThat(result).isEqualTo(mockTicket);
    }
    
    @Test
    public void shouldCallBookingFacadeCancelTicket_whenPerformCancelTicket() throws IOException {
        ticketController.cancelTicket(TICKET_ID, mockHttpServletResponse);
        verify(mockBookingFacade).cancelTicket(TICKET_ID);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformCancelTicketAndTicketIsNotCanceled() throws IOException {
        when(mockBookingFacade.cancelTicket(TICKET_ID)).thenReturn(false);
        ticketController.cancelTicket(TICKET_ID, mockHttpServletResponse);
        verify(mockHttpServletResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }

}
