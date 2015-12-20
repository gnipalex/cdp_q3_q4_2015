package com.epam.hnyp.springbooking.dao.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;

@Repository("ticketDao")
public class TicketDaoImpl extends AbstractJdbcDao implements TicketDao {

    private static final String SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE = 
            "SELECT * FROM `ticket` AS t "
            + "JOIN `event` AS e ON t.eventId = e.id "
            + "WHERE t.userId = ? "
            + "ORDER BY e.date DESC "
            + "LIMIT ?,?";
    private static final String SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL = 
            "SELECT * FROM `ticket` AS t "
            + "JOIN `user` AS u ON t.userId = u.id "
            + "WHERE u.eventId=? "
            + "ORDER BY u.email ASC";
    private static final String SELECT_BY_ID_AND_PLACE = 
            "SELECT * FROM `ticket` AS t "
            + "WHERE t.id=? AND t.place=?";
    private static final String INSERT_TICKET = 
            "INSERT INTO `ticket`(place,category,eventId,userId) "
            + "VALUE (?,?,?,?)"; 
    private static final String DELETE_BY_ID = 
            "DELETE FROM `ticket` AS t WHERE t.id=?";
     
    @Override
    public Ticket create(Ticket ticket) {
        Object[] args = { ticket.getPlace(), ticket.getCategory(), 
                ticket.getEventId(), ticket.getUserId() };
        Number key = updateAndGetKey(INSERT_TICKET, args);
        if (key != null) {
            ticket.setId(key.longValue());
            return ticket;
        }
        return null;
    }
    
    @Lookup("ticketInstance")
    protected Ticket createTicketInstance() {
        throw new UnsupportedOperationException();
    }
    
    private RowMapper<Ticket> getTicketMapper() {
        return (rs, rowNumber) -> {
            Ticket ticket = createTicketInstance();
            ticket.setId(rs.getLong("id"));
            Category category = Category.valueOf(rs.getString("category"));
            ticket.setCategory(category);
            ticket.setEventId(rs.getLong("eventId"));
            ticket.setUserId(rs.getLong("userId"));
            ticket.setPlace(rs.getInt("palce"));
            return ticket;
        };
    }

    @Override
    public List<Ticket> getAllByUserIdSortedByEventDate(long userId,
            int pageSize, int pageNum) {
        Object[] args = { userId, getOffset(pageSize, pageNum), pageSize };
        return getJdbcTemplate().query(SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE, 
                getTicketMapper(), args);
    }

    @Override
    public List<Ticket> getAllByEventIdSortedByUserEmail(long eventId,
            int pageSize, int pageNum) {
        Object[] args = { eventId, getOffset(pageSize, pageNum), pageSize };
        return getJdbcTemplate().query(SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL,
                getTicketMapper(), args);
    }

    @Override
    public Ticket getByEventIdAndPlace(long eventId, int place,
            Category category) {
        return getJdbcTemplate().queryForObject(SELECT_BY_ID_AND_PLACE, 
                getTicketMapper(), eventId, place);
    }

    @Override
    public boolean delete(long ticketId) {
        return getJdbcTemplate().update(DELETE_BY_ID, ticketId) > 0;
    }

}
