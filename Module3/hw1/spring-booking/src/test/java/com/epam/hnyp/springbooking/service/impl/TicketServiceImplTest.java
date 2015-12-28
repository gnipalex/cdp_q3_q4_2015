package com.epam.hnyp.springbooking.service.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.service.UserAccountService;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    private static final long EVENT_ID = 7L;
    private static final long USER_ID = 10L;
    private static final Ticket.Category CATEGORY = Category.BAR;
    private static final BigDecimal TICKET_PRICE = BigDecimal.TEN;
    private static final int PLACE = 22;
    
    @Mock
    private UserAccountService mockAccountService;
    @Mock
    private TicketDao mockTicketDao;
    @Mock
    private User mockUser;
    @Mock
    private Event mockEvent;
    @Mock
    private Ticket mockTicket;
    @Mock
    private UserAccount mockUserAccount;
    
    @InjectMocks
    @Spy
    private TicketServiceImpl spyTicketService;
    
    @Before
    public void setUp() {
        when(mockEvent.getId()).thenReturn(EVENT_ID);
        when(mockEvent.getTicketPrice()).thenReturn(TICKET_PRICE);
        when(mockUser.getId()).thenReturn(USER_ID); 
        when(mockUserAccount.getUserId()).thenReturn(USER_ID);
        doReturn(mockTicket).when(spyTicketService).createTicketInstance();
        
        when(mockAccountService.getAccountByUserId(USER_ID)).thenReturn(mockUserAccount);

    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformBookTicketAndPlaceIsNotFree() {
        when(mockTicketDao.getByIdAndPlaceAndCategory(EVENT_ID, PLACE, CATEGORY)).thenReturn(mockTicket);
        spyTicketService.bookTicket(mockUser, mockEvent, PLACE, CATEGORY);
    }
    
    @Test
    public void shouldRetrieveAccount_whenPerformBookTicket() {
        spyTicketService.bookTicket(mockUser, mockEvent, PLACE, CATEGORY);
        verify(mockAccountService).getAccountByUserId(USER_ID);
    }
    
    @Test
    public void shouldPerformWithdrawAmount_whenPerformBookTicket() {
        spyTicketService.bookTicket(mockUser, mockEvent, PLACE, CATEGORY);
        verify(mockAccountService).withdrawAmountFromAccount(mockUserAccount, TICKET_PRICE);
    }
    
    @Test
    public void shouldCreateTicketAndSetFields_whenPerformCreateTicket() {
        spyTicketService.bookTicket(mockUser, mockEvent, PLACE, CATEGORY);
        verify(mockTicket).setCategory(CATEGORY);
        verify(mockTicket).setEventId(EVENT_ID);
        verify(mockTicket).setPlace(PLACE);
        verify(mockTicket).setUserId(USER_ID);
    }
    
    @Test
    public void shouldUseInjectedTicketInstance_whenPerformBookTicket() {
        spyTicketService.bookTicket(mockUser, mockEvent, PLACE, CATEGORY);
        verify(mockTicketDao).create(mockTicket);
    }

}
