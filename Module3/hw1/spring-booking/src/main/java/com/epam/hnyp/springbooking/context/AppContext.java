package com.epam.hnyp.springbooking.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;

import com.epam.hnyp.springbooking.model.Event;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.model.impl.EventImpl;
import com.epam.hnyp.springbooking.model.impl.TicketImpl;
import com.epam.hnyp.springbooking.model.impl.UserAccountImpl;
import com.epam.hnyp.springbooking.model.impl.UserImpl;

@Configuration
public class AppContext {

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

    public Event createEventInstance() {
        return new EventImpl();
    }
    
    @Bean(name = "userRowMapper")
    public RowMapper<User> getUserRowMapper() {
        return (rs, rowNumber) -> {
            User user = createUserInstance();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            return user;
        };
    }
    
    @Bean(name = "eventRowMapper")
    public RowMapper<Event> getEventRowMapper() {
        return (rs, rowNumber) -> {
            Event event = createEventInstance();
            event.setId(rs.getLong("id"));
            event.setDate(rs.getDate("date"));
            event.setTicketPrice(rs.getBigDecimal("ticketPrice"));
            event.setTitle(rs.getString("title"));
            return event;
        };
    }
    
    @Bean(name = "ticketRowMapper")
    public RowMapper<Ticket> getTicketRowMapper() {
        return (rs, rowNumber) -> {
            Ticket ticket = createTicketInstance();
            ticket.setId(rs.getLong("id"));
            Category category = Category.valueOf(rs.getString("category"));
            ticket.setCategory(category);
            ticket.setEventId(rs.getLong("eventId"));
            ticket.setUserId(rs.getLong("userId"));
            ticket.setPlace(rs.getInt("place"));
            return ticket;
        };
    }
    
    @Bean(name = "userAccountRowMapper")
    public RowMapper<UserAccount> getUserAccountRowMapper() {
        return (rs, rowNumber) -> {
            UserAccount account = createUserAccountInstance();
            account.setUserId(rs.getLong("userId"));
            account.setPrepaidAmount(rs.getBigDecimal("prepaidAmount"));
            return account;
        };
    }

}
