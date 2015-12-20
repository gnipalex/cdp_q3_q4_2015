package com.epam.hnyp.springbooking.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by maksym_govorischev.
 */
public interface Event {
    /**
     * Event id. UNIQUE.
     * @return Event Id
     */
    long getId();
    void setId(long id);
    String getTitle();
    void setTitle(String title);
    Date getDate();
    void setDate(Date date);
    BigDecimal getTicketPrice();
    void setTicketPrice(BigDecimal ticketPrice);
}
