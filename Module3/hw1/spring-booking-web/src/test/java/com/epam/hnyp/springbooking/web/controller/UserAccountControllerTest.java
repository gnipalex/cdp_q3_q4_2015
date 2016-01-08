package com.epam.hnyp.springbooking.web.controller;

import static java.math.BigDecimal.valueOf;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.UserAccount;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountControllerTest {

    private static final long USER_ID = 1;
    private static final BigDecimal AMOUNT = valueOf(2.1);
    
    @Mock
    private BookingFacade mockBookingFacade;
    @Mock
    private UserAccount mockUserAccount;
    
    @InjectMocks
    private UserAccountController userAccountController;
    
    @Test
    public void shouldCallBookingFacadeGetUSerAccount_whenPerformGetUserAccount() {
        userAccountController.getUserAccount(USER_ID);
        verify(mockBookingFacade).getUserAccount(USER_ID);
    }
    
    @Test
    public void shouldReturnUserAccount_whenPerformGetUserAccount() {
        when(mockBookingFacade.getUserAccount(USER_ID)).thenReturn(mockUserAccount);
        UserAccount result = userAccountController.getUserAccount(USER_ID);
        assertThat(result).isEqualTo(mockUserAccount);
    }
    
    @Test
    public void shouldCallBookingFacadeRefillUsersAccount_whenPerformRefillUserAccount() {
        userAccountController.refillUserAccount(USER_ID, AMOUNT);
        verify(mockBookingFacade).refillUsersAccount(USER_ID, AMOUNT);
    }

}
