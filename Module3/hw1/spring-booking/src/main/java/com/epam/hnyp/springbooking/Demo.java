package com.epam.hnyp.springbooking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.epam.hnyp.springbooking.facade.BookingFacade;

public class Demo {
	
	private static final String APPLICATION_CONTEXT = "application-context.xml";
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT);
		BookingFacade bookingFacade = ctx.getBean("bookingFacade", BookingFacade.class);
		System.out.println(bookingFacade);
	}
	
}
