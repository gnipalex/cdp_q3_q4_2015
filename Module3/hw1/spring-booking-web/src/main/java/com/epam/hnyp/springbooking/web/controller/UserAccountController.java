package com.epam.hnyp.springbooking.web.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.UserAccount;

@RequestMapping("/users/{userId}/account")
@Controller
public class UserAccountController {

    @Autowired
    private BookingFacade bookingFacade;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public UserAccount getUserAccount(@PathVariable long userId) {
        return bookingFacade.getUserAccount(userId);
    }
    
    @RequestMapping(value = "/refill", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void refillUserAccount(@PathVariable long userId, @RequestParam BigDecimal amount) {
        bookingFacade.refillUsersAccount(userId, amount);
    }
}
