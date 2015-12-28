package com.epam.hnyp.springbooking.dao.db;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.TicketDao;
import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;

@Repository("ticketDao")
public class TicketDaoImpl extends AbstractJdbcDao<Ticket> implements TicketDao {

    private static final String SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE = 
            "SELECT * FROM `ticket` AS t "
            + "JOIN `event` AS e ON t.eventId = e.id "
            + "WHERE t.userId = ? "
            + "ORDER BY e.date DESC "
            + "LIMIT ?,?";
    private static final String SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL = 
            "SELECT * FROM `ticket` AS t "
            + "JOIN `user` AS u ON t.userId = u.id "
            + "WHERE t.eventId=? "
            + "ORDER BY u.email ASC "
            + "LIMIT ?,?";
    private static final String SELECT_BY_ID_AND_PLACE_AND_CATEGORY = 
            "SELECT * FROM `ticket` AS t "
            + "WHERE t.id=? AND t.place=? AND t.category=?";
    private static final String INSERT_TICKET = 
            "INSERT INTO `ticket`(place,category,eventId,userId) "
            + "VALUE (?,?,?,?)"; 
    private static final String DELETE_BY_ID = 
            "DELETE FROM `ticket` WHERE id=?";
    
    @Resource
    private RowMapper<Ticket> ticketRowMapper;
     
    @Override
    public Ticket create(Ticket ticket) {
        Object[] args = { ticket.getPlace(), String.valueOf(ticket.getCategory()), 
                ticket.getEventId(), ticket.getUserId() };
        Number key = updateAndGetKey(INSERT_TICKET, args);
        ticket.setId(key.longValue());
        return ticket;
    }

    @Override
    public List<Ticket> getAllByUserIdSortedByEventDate(long userId, int pageSize, int pageNum) {
        Object[] args = { userId, getOffset(pageSize, pageNum), pageSize };
        return queryForList(SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE, args);
    }

    @Override
    public List<Ticket> getAllByEventIdSortedByUserEmail(long eventId, int pageSize, int pageNum) {
        Object[] args = { eventId, getOffset(pageSize, pageNum), pageSize };
        return queryForList(SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL, args);
    }

    @Override
    public Ticket getByIdAndPlaceAndCategory(long eventId, int place, Category category) {
        Object[] args = { eventId, place, String.valueOf(category) }; 
        return queryForObject(SELECT_BY_ID_AND_PLACE_AND_CATEGORY, args);
    }

    @Override
    public boolean delete(long ticketId) {
        return getJdbcTemplate().update(DELETE_BY_ID, ticketId) > 0;
    }

    @Override
    protected RowMapper<Ticket> getRowMapper() {
        return ticketRowMapper;
    }
   
}
