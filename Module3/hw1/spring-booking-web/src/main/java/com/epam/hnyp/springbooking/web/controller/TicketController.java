package com.epam.hnyp.springbooking.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;

@RequestMapping("/tickets")
@Controller
public class TicketController {

    @Autowired
    private BookingFacade bookingFacade;
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Ticket bookTicket(TicketImpl ticket) {
        return bookingFacade.bookTicket(ticket.getUserId(), ticket.getEventId(), 
                ticket.getPlace(), ticket.getCategory());
    }
    
    @RequestMapping(value = "/{ticketId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void cancelTicket(@PathVariable long ticketId, HttpServletResponse response) throws IOException {
        if (!bookingFacade.cancelTicket(ticketId)) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "ticket is not canceled");
        }
    }
    
}
