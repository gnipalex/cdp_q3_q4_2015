package com.epam.hnyp.springbooking.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.hnyp.springbooking.facade.BookingFacade;
import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.web.Constants;

@RequestMapping("/events")
@Controller
public class EventController {

    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    
    @Autowired
    private BookingFacade bookingFacade;
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Event createEvent(EventImpl event) {
        return bookingFacade.createEvent(event);
    }
    
    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Event getEventById(@PathVariable long eventId) {
        return bookingFacade.getEventById(eventId);
    }
    
    @RequestMapping(value = "/{eventId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Event updateEvent(@PathVariable long eventId, EventImpl event) {
        event.setId(eventId);
        return bookingFacade.updateEvent(event);
    }
    
    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEvent(@PathVariable long eventId, HttpServletResponse response) throws IOException {
        if (!bookingFacade.deleteEvent(eventId)) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "event is not deleted");
        }
    }
    
    @RequestMapping(value = "/{title}", params = "byTitle=true", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Event> getEventsByTitle(@PathVariable String title,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum) {
        return bookingFacade.getEventsByTitle(title, pageSize, pageNum);
    }
    
    @RequestMapping(value = "/{day}", params = "byDay=true", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Event> getEventsForDay(@PathVariable Date day,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum) {
        return bookingFacade.getEventsForDay(day, pageSize, pageNum);
    }
    
    @InitBinder
    public void bindCustomDateEditor(WebDataBinder binder) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        CustomDateEditor customDateEditor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }
    
    @RequestMapping(value = "/{eventId}/tickets", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Ticket> getTicketsForEvent(@PathVariable long eventId,
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize, 
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int pageNum,
            HttpServletResponse response) throws IOException {
        Event event = getEventById(eventId);
        if (event == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "event not found");
            return null;
        }
        return bookingFacade.getBookedTickets(event, pageSize, pageNum);
    }
     
}
