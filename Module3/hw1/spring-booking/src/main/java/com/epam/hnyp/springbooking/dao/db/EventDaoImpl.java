package com.epam.hnyp.springbooking.dao.db;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.EventDao;
import com.epam.hnyp.springbooking.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractJdbcDao<Event> implements EventDao {

    private static final String SELECT_BY_ID = "SELECT * FROM `event` AS e WHERE e.id=?";
    private static final String SELECT_ALL_BY_TITLE = "SELECT * FROM `event` AS e WHERE e.title LIKE ? LIMIT ?,?";
    private static final String SELECT_ALL_BY_DATE = "SELECT * FROM `event` AS e WHERE e.date=? LIMIT ?,?";
    private static final String INSERT_EVENT = "INSERT INTO `event`(title,date,ticketPrice) VALUE (?,?,?)";
    private static final String UPDATE_EVENT = "UPDATE `event` SET title=?,date=?,ticketPrice=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `event` WHERE id=?";

    @Resource
    private RowMapper<Event> eventRowMapper;
    
    @Override
    public Event getById(long eventId) {
        return queryForObject(SELECT_BY_ID, eventId);
    }

    @Override
    public List<Event> getAllByTitle(String title, int pageSize, int pageNum) {
        Object[] args = { formatTitle(title), getOffset(pageSize, pageNum), pageSize };
        return queryForList(SELECT_ALL_BY_TITLE, args);
    }
    
    private String formatTitle(String title) {
    	return "%" + title + "%";
    }

    @Override
    public List<Event> getAllForDay(Date day, int pageSize, int pageNum) {
        Object[] args = { day, getOffset(pageSize, pageNum), pageSize };
        return queryForList(SELECT_ALL_BY_DATE, args);
    }

    @Override
    public Event create(Event event) {
        Object[] args = { event.getTitle(), event.getDate(), event.getTicketPrice() };
        Number key = updateAndGetKey(INSERT_EVENT, args);
        event.setId(key.longValue());
        return event;
    }

    @Override
    public void update(Event event) {
        Object[] args = { event.getTitle(), event.getDate(), event.getTicketPrice(), event.getId() };
        updateRow(UPDATE_EVENT, args);
    }

    @Override
    public boolean delete(long eventId) {
        return getJdbcTemplate().update(DELETE_BY_ID, eventId) > 0;
    }

    @Override
    protected RowMapper<Event> getRowMapper() {
        return eventRowMapper;
    }

}
