package com.epam.hnyp.springbooking.web.controller;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.impl.UserImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final long USER_ID = 1;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 3;
    private static final String EMAIL = "test@email.com";
    private static final String USER_NAME = "name";
    
    private static final String USER_LIST_VIEW = "user/list";

    @Mock
    private BookingFacade mockBookingFacade;
    @Mock
    private UserImpl mockUser;
    @Mock
    private Ticket mockTicket;
    @Mock
    private HttpServletResponse mockHttpServletResponse;
    @Mock
    private Model mockModel;
    
    @InjectMocks
    private UserController userController;
    
    @Test
    public void shouldCallBookingFacadeGetBookedTickets_whenPerformGetTicketsForUser() throws IOException {
        when(mockBookingFacade.getUserById(USER_ID)).thenReturn(mockUser);
        userController.getTicketsForUser(USER_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpServletResponse);
        verify(mockBookingFacade).getBookedTickets(mockUser, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldReturnTickets_whenPerformGetTicketsForUser() throws IOException {
        when(mockBookingFacade.getUserById(USER_ID)).thenReturn(mockUser);
        when(mockBookingFacade.getBookedTickets(mockUser, PAGE_SIZE, PAGE_NUMBER)).thenReturn(asList(mockTicket));
        List<Ticket> results = userController.getTicketsForUser(USER_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpServletResponse);
        assertThat(results).containsOnly(mockTicket);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformGetTicketsForUserAndUserIsNotFound() throws IOException {
        when(mockBookingFacade.getUserById(USER_ID)).thenReturn(null);
        userController.getTicketsForUser(USER_ID, PAGE_SIZE, PAGE_NUMBER, mockHttpServletResponse);
        verify(mockHttpServletResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }
    
    @Test
    public void shouldReturnUser_whenPerformGetUserById() throws IOException {
        when(mockBookingFacade.getUserById(USER_ID)).thenReturn(mockUser);
        User result = userController.getUserById(USER_ID, mockHttpServletResponse);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformGetUserByIdAndUserIsNotFound() throws IOException {
        when(mockBookingFacade.getUserById(USER_ID)).thenReturn(null);
        userController.getUserById(USER_ID, mockHttpServletResponse);
        verify(mockHttpServletResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }
    
    @Test
    public void shouldCallBookingFacadeUpdateUser_whenPerformUpdateUserById() {
        userController.updateUserById(USER_ID, mockUser, mockHttpServletResponse);
        verify(mockBookingFacade).updateUser(mockUser);
    }
    
    @Test
    public void shouldSetIdToUserModel_whenPerformUpdateUserById() {
        userController.updateUserById(USER_ID, mockUser, mockHttpServletResponse);
        verify(mockUser).setId(USER_ID);
    }
    
    @Test
    public void shouldReturnUpdatedUser_whenPerformUpdateUserById() {
        when(mockBookingFacade.updateUser(mockUser)).thenReturn(mockUser);
        User result = userController.updateUserById(USER_ID, mockUser, mockHttpServletResponse);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldCallBookingFacadeDeleteUser_whenPerformDeleteUserById() throws IOException {
        userController.deleteUserById(USER_ID, mockHttpServletResponse);
        verify(mockBookingFacade).deleteUser(USER_ID);
    }
    
    @Test
    public void shouldSendErrorWithNotFoundStatus_whenPerformDeleteUserByIdAndUserIsNotDeleted() throws IOException {
        when(mockBookingFacade.deleteUser(USER_ID)).thenReturn(false);
        userController.deleteUserById(USER_ID, mockHttpServletResponse);
        verify(mockHttpServletResponse).sendError(eq(NOT_FOUND.value()), anyString());
    }
    
    @Test
    public void shouldCallBookingFacadeCreateUser_whenPerformCreateUser() {
        userController.createUser(mockUser);
        verify(mockBookingFacade).createUser(mockUser);
    }
    
    @Test
    public void shouldReturnCreatedUser_whenPerfromCreateUser() {
        when(mockBookingFacade.createUser(mockUser)).thenReturn(mockUser);
        User result = userController.createUser(mockUser);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldCallBookingFacadeGetUserByEmail_whenPerformGetUserByEmail() {
        userController.getUserByEmail(EMAIL);
        verify(mockBookingFacade).getUserByEmail(EMAIL);
    }
    
    @Test
    public void shouldReturnUser_whenPerformGetUserByEmail() {
        when(mockBookingFacade.getUserByEmail(EMAIL)).thenReturn(mockUser);
        User result = userController.getUserByEmail(EMAIL);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldCallBookingFacadeGetUsersByName_whenPerfromGetUsersByName() {
        userController.getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER);
        verify(mockBookingFacade).getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldReturnUsers_whenPerformGetUsersByName() {
        when(mockBookingFacade.getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER)).thenReturn(asList(mockUser));
        List<User> results = userController.getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER);
        assertThat(results).containsOnly(mockUser);
    }
    
    @Test
    public void shouldCallBookingFacadeGetUsersByName_whenPerformGetUsersByNameInHtml() {
        userController.getUsersByNameInHtml(USER_NAME, PAGE_SIZE, PAGE_NUMBER, mockModel);
        verify(mockBookingFacade).getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER);
    }
    
    @Test
    public void shouldSetUserNameAttribute_whenPerformGetUsersByNameInHtml() {
        userController.getUsersByNameInHtml(USER_NAME, PAGE_SIZE, PAGE_NUMBER, mockModel);
        verify(mockModel).addAttribute("userName", USER_NAME);
    }
    
    @Test
    public void shouldSetUsersAttribute_whenPerformGetUsersByNameInHtml() {
        List<User> users = asList(mockUser);
        when(mockBookingFacade.getUsersByName(USER_NAME, PAGE_SIZE, PAGE_NUMBER)).thenReturn(users);
        userController.getUsersByNameInHtml(USER_NAME, PAGE_SIZE, PAGE_NUMBER, mockModel);
        verify(mockModel).addAttribute("users", users);
    }
    
    @Test
    public void shouldReturnUserListView_whenPerformGetUsersByNameInHtml() {
        String result = userController.getUsersByNameInHtml(USER_NAME, PAGE_SIZE, PAGE_NUMBER, mockModel);
        assertThat(result).isEqualTo(USER_LIST_VIEW);
    }   

}
