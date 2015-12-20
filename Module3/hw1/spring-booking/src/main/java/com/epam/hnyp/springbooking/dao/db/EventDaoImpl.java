package com.epam.hnyp.springbooking.dao.db;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractJdbcDao implements EventDao {

    private static final String SELECT_BY_ID = "SELECT * FROM `event` AS e WHERE e.id=?";
    private static final String SELECT_ALL_BY_TITLE = "SELECT * FROM `event` AS e WHERE e.title LIKE ? LIMIT ?,?";
    private static final String SELECT_ALL_BY_DATE = "SELECT * FROM `event` AS e WHERE e.date=? LIMIT ?,?";
    private static final String INSERT_EVENT = "INSERT INTO `event`(title,date,ticketPrice) VALUE (?,?,?)";
    private static final String UPDATE_EVENT = "UPDATE `event` SET title=?,date=?,ticketPrice=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `event` AS e WHERE e.id=?";

    @Override
    public Event getById(long eventId) {
        return getJdbcTemplate().queryForObject(SELECT_BY_ID,
                getEventMapper(), eventId);
    }

    private RowMapper<Event> getEventMapper() {
        return (rs, rowNumber) -> {
            Event event = createEventInstance();
            event.setId(rs.getLong("id"));
            event.setDate(rs.getDate("date"));
            event.setTicketPrice(rs.getBigDecimal("ticketPrice"));
            event.setTitle(rs.getString("title"));
            return event;
        };
    }

    @Lookup("eventInstance")
    protected Event createEventInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> getAllByTitle(String title, int pageSize, int pageNum) {
        Object[] args = { title, getOffset(pageSize, pageNum), pageSize };
        return getJdbcTemplate().query(SELECT_ALL_BY_TITLE, 
                getEventMapper(), args);
    }

    @Override
    public List<Event> getAllForDay(Date day, int pageSize, int pageNum) {
        Object[] args = { day, getOffset(pageSize, pageNum), pageSize };
        return getJdbcTemplate().query(SELECT_ALL_BY_DATE, getEventMapper(), args);
    }

    @Override
    public Event create(Event event) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Object[] args = { event.getTitle(), event.getDate(), event.getTicketPrice() };
        if (updateWithKeyHolder(INSERT_EVENT, keyHolder, args) > 0) {
            event.setId(keyHolder.getKey().longValue());
            return event;
        }
        return null;
    }

    @Override
    public Event update(Event event) {
        Object[] args = { event.getTitle(), event.getDate(), event.getTicketPrice(), event.getId() };
        if (getJdbcTemplate().update(UPDATE_EVENT, args) > 1) {
            return event;
        }
        return null;
    }

    @Override
    public boolean delete(long eventId) {
        return getJdbcTemplate().update(DELETE_BY_ID, eventId) > 0;
    }

}
