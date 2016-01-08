package com.epam.hnyp.springbooking.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.impl.UserImpl;
import com.epam.hnyp.springbooking.web.Constants;

@RequestMapping("/users")
@Controller
public class UserController {

    private static final String USER_LIST_VIEW = "user/list";
    
    @Autowired
    private BookingFacade bookingFacade;
    
    @RequestMapping(value = "/{userId}/tickets", method = RequestMethod.GET)
    @ResponseBody
    public List<Ticket> getTicketsForUser(@PathVariable long userId,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum,
            HttpServletResponse response) throws IOException {
        User user = getUserById(userId, response);
        if (user == null) {
            return null;
        }
        return bookingFacade.getBookedTickets(user, pageSize, pageNum);
    }
    
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserById(@PathVariable long userId, 
            HttpServletResponse response) throws IOException {
        User user = bookingFacade.getUserById(userId);
        if (user == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "user not found");
            return null;
        }
        return user;
    }
    
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUserById(@PathVariable long userId, UserImpl user, 
            HttpServletResponse response) {
        user.setId(userId);
        return bookingFacade.updateUser(user);
    }
    
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteUserById(@PathVariable long userId, HttpServletResponse response) throws IOException {
        if (!bookingFacade.deleteUser(userId)) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "user is not deleted");
        }
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(UserImpl user) {
        return bookingFacade.createUser(user);
    }
    
    @RequestMapping(value = "/find", params = "byEmail", method = RequestMethod.GET)
    @ResponseBody
    public User getUserByEmail(@RequestParam(value="byEmail") String email) {
        return bookingFacade.getUserByEmail(email);
    }
    
    @RequestMapping(value = "/filter", params = "byName", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersByName(@RequestParam(value="byName") String userName,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum) {
        return bookingFacade.getUsersByName(userName, pageSize, pageNum);
    }
    
    @RequestMapping(value = "/filter", params = "byName", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getUsersByNameInHtml(@RequestParam(value="byName") String userName,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum,
            Model model) {
        List<User> users = getUsersByName(userName, pageSize, pageNum);
        model.addAttribute("userName", userName);
        model.addAttribute("users", users);
        return USER_LIST_VIEW;
    }
    
}
