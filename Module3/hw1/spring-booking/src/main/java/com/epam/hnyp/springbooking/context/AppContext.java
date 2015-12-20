package com.epam.hnyp.springbooking.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;
import com.epam.hnyp.springbooking.model.impl.UserAccountImpl;
import com.epam.hnyp.springbooking.model.impl.UserImpl;

@Configuration
public class AppContext {

    @Bean(name = "userInstance")
    @Scope(value = "prototype")
    public User createUserInstance() {
        return new UserImpl();
    }

    @Bean(name = "userAccountInstance")
    @Scope(value = "prototype")
    public UserAccount createUserAccountInstance() {
        return new UserAccountImpl();
    }

    @Bean(name = "ticketInstance")
    @Scope(value = "prototype")
    public Ticket createTicketInstance() {
        return new TicketImpl();
    }

    @Bean(name = "eventInstance")
    @Scope(value = "prototype")
    public Event createEventInstance() {
        return new EventImpl();
    }

}
