package com.epam.hnyp.springbooking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.hnyp.springbooking.facade.BookingFacade;

public class Demo {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/application-context.xml");
        BookingFacade bookingFacade = ctx.getBean(BookingFacade.class);
        System.out.println(bookingFacade);
    }
}
